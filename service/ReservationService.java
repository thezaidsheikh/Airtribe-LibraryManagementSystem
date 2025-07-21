package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.utils;
import model.Book;
import model.Member;
import model.PhysicalBook;
import model.Reservation;

/**
 * ReservationService class handles all book reservation operations in the
 * library management system.
 * This service manages the reservation queue, validates reservation
 * eligibility, and maintains
 * reservation data persistence. It works in conjunction with BookService and
 * MemberService
 * to ensure proper book availability tracking and member validation.
 * 
 * Key Features:
 * - Book reservation with member and book validation
 * - Duplicate reservation prevention
 * - Physical book copy management (reserved vs available)
 * - Reservation queue management with FIFO ordering
 * - Data persistence with serialization support
 * - Integration with member borrowing policies
 * 
 * The service automatically updates book availability when reservations are
 * made,
 * reducing available copies and increasing reserved copies for physical books.
 * Digital books (EBooks and AudioBooks) are always available and don't require
 * copy management.
 * 
 * @author Zaid Sheikh
 * @version 1.0
 * @since 2025-01-09
 */
public class ReservationService implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Scanner instance for reading user input from console */
    Scanner scn = new Scanner(System.in);

    /** Reference to MemberService for member validation and operations */
    MemberService memberService;

    /** Reference to BookService for book validation and inventory management */
    BookService bookService;

    /** List containing all active reservations in the system */
    List<Reservation> reservations = new ArrayList<>();

    /**
     * Loads reservation data from the serialized database file.
     * This method reads the reservation records from the persistent storage
     * and populates the in-memory reservation list. If the file doesn't exist
     * or is corrupted, an empty list is returned.
     * 
     * @throws Exception if there's an error reading the reservation data file
     */
    public void loadReservationData() throws Exception {
        this.reservations = utils.loadData("./db/reservations.ser");
    }

    /**
     * Constructs a new ReservationService with references to required services.
     * This constructor initializes the service with dependencies needed for
     * reservation operations, including member validation and book management.
     * 
     * @param memberService The MemberService instance for member-related operations
     * @param bookService   The BookService instance for book-related operations
     */
    public ReservationService(MemberService memberService, BookService bookService) {
        this.memberService = memberService;
        this.bookService = bookService;
    }

    /**
     * Checks if a specific member has already reserved a specific book.
     * This method prevents duplicate reservations by the same member for the same
     * book,
     * ensuring that each member can only have one active reservation per book.
     * This helps maintain fair access to limited resources and prevents queue
     * manipulation.
     * 
     * @param memberId The unique identifier of the member to check
     * @param bookId   The unique identifier (ISBN) of the book to check
     * @return true if the member has already reserved this book, false otherwise
     */
    protected boolean isAlreadyReservedByMemberIdAndBookId(long memberId, long bookId) {
        for (Reservation reservation : this.reservations) {
            if (reservation.getMemberId() == memberId && reservation.getBookId() == bookId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a specific reservation from the reservation list.
     * This method is typically called when a book is issued to a member who had
     * reserved it, or when a member cancels their reservation. It uses a lambda
     * expression with removeIf for efficient removal based on member and book IDs.
     * 
     * @param memberId The unique identifier of the member whose reservation to
     *                 remove
     * @param bookId   The unique identifier (ISBN) of the book reservation to
     *                 remove
     */
    protected void removeReservation(long memberId, long bookId) {
        this.reservations
                .removeIf(reservation -> reservation.getMemberId() == memberId && reservation.getBookId() == bookId);
    }

    /**
     * Saves the current reservation list to the database files.
     * This method persists all reservation data to both text and serialized formats
     * for data durability and backup purposes. The text file provides
     * human-readable
     * format while the serialized file enables efficient object loading.
     * 
     * @throws Exception if there's an error writing to the database files
     */
    protected void updateReservationToDatabase() throws Exception {
        utils.saveData("./db/reservations.txt", this.reservations);
    }

    /**
     * Retrieves the first (oldest) reservation for a specific book.
     * This method implements a First-In-First-Out (FIFO) queue system for
     * reservations,
     * ensuring fair access to books by serving reservations in the order they were
     * made.
     * This is typically used when a book becomes available and needs to be
     * allocated
     * to the next member in the reservation queue.
     * 
     * @param bookId The unique identifier (ISBN) of the book to find reservations
     *               for
     * @return The first Reservation object for the specified book, or null if no
     *         reservations exist
     */
    protected Reservation getFirstReservationByBookId(long bookId) {
        for (Reservation reservation : this.reservations) {
            if (reservation.getBookId() == bookId) {
                return reservation;
            }
        }
        return null;
    }

    /**
     * Processes a book reservation request from a library member.
     * This method handles the complete reservation workflow including member
     * validation,
     * book availability checking, duplicate reservation prevention, and inventory
     * updates.
     * 
     * The reservation process follows these steps:
     * 1. Validates member ID and retrieves member information
     * 2. Checks if member is eligible to borrow books (active status, within
     * limits, no excessive fines)
     * 3. Validates book ID and retrieves book information
     * 4. For physical books, verifies availability (available copies > 0)
     * 5. Prevents duplicate reservations by the same member for the same book
     * 6. Creates a new reservation record with current timestamp
     * 7. Updates physical book inventory (decreases available, increases reserved
     * copies)
     * 8. Persists changes to both book and reservation databases
     * 
     * Digital books (EBooks and AudioBooks) are always considered available and
     * don't
     * require copy management, but reservations are still tracked for reporting
     * purposes.
     * 
     * Member eligibility is determined by:
     * - Active membership status (not suspended or expired)
     * - Current borrowed books within borrowing limit
     * - Total fine amount below maximum threshold
     * - Renewal count within allowed limits
     * 
     * @throws Exception if member ID is invalid or negative
     * @throws Exception if member is not found in the system
     * @throws Exception if member is not eligible to borrow books
     * @throws Exception if book ID is invalid or negative
     * @throws Exception if book is not found in the system
     * @throws Exception if physical book has no available copies
     * @throws Exception if member has already reserved this book
     * @throws Exception if there's an error updating the database
     */
    public void reserveBook() throws Exception {
        System.out.print("Enter member ID: ");
        long memberId = Long.parseLong(scn.nextLine());
        if (memberId < 0) {
            throw new Exception("Invalid member ID");
        }
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            throw new Exception("Member not found");
        }
        if (!member.canBorrowBooks()) {
            throw new Exception("This member can't able to borrow books");
        }

        System.out.print("Enter book ID: ");
        long bookId = Long.parseLong(scn.nextLine());
        if (bookId < 0) {
            throw new Exception("Invalid book ID");
        }

        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new Exception("Book not found");
        }
        if (book instanceof PhysicalBook) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            if (!physicalBook.isAvailable()) {
                throw new Exception("Book is not available");
            }
        }

        if (this.isAlreadyReservedByMemberIdAndBookId(memberId, bookId)) {
            throw new Exception("This member has already reserved this book");
        }

        Reservation reservation = new Reservation(memberId, bookId);
        if (book instanceof PhysicalBook) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            physicalBook.setReservedCopies(physicalBook.getReservedCopies() + 1);
            physicalBook.setAvailableCopies(physicalBook.getAvailableCopies() - 1);
        }
        this.reservations.add(reservation);
        this.bookService.updateBookInList(book);
        this.bookService.updateBookInDatabase();

        this.updateReservationInDatabase();
    }

    /**
     * Persists the current reservation data to the database files.
     * This method is a duplicate of updateReservationToDatabase() and serves
     * the same purpose of saving reservation data to both text and serialized
     * formats.
     * It ensures data consistency and provides backup mechanisms for reservation
     * records.
     * 
     * The method saves to:
     * - reservations.txt: Human-readable text format for manual inspection
     * - reservations.ser: Serialized binary format for efficient loading
     * 
     * @throws Exception if there's an error writing to the database files
     */
    protected void updateReservationInDatabase() throws Exception {
        utils.saveData("./db/reservations.txt", this.reservations);
    }
}
