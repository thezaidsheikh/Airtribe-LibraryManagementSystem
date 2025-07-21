package service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import common.MemberPolicy;
import common.utils;
import model.Book;
import model.BookIssue;
import model.Member;
import model.PhysicalBook;
import model.Reservation;

/**
 * Service class for managing book issuance, returns, and related operations in
 * the library system.
 * <p>
 * This class handles the core functionality for issuing books to members,
 * processing returns,
 * managing renewals, and generating various reports. It works in conjunction
 * with
 * MemberService, BookService, and ReservationService to maintain data
 * consistency.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2023-01-01
 * @see MemberService
 * @see BookService
 * @see ReservationService
 * @see BookIssue
 */
public class BookIssueService {
    private Scanner scn = new Scanner(System.in);
    private MemberService memberService;
    private BookService bookService;
    private ReservationService reservationService;
    private List<BookIssue> bookIssued = new ArrayList<>();

    /**
     * Constructor to initialize the memberService and bookService
     * 
     * @param memberService
     * @param bookService
     */
    /**
     * Constructs a new BookIssueService with the specified service dependencies.
     *
     * @param memberService      the service for member-related operations
     * @param bookService        the service for book-related operations
     * @param reservationService the service for handling book reservations
     * @throws IllegalArgumentException if any of the services are null
     */
    public BookIssueService(MemberService memberService, BookService bookService,
            ReservationService reservationService) {
        if (memberService == null || bookService == null || reservationService == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.memberService = memberService;
        this.bookService = bookService;
        this.reservationService = reservationService;
    }

    /**
     * Loads book issue records from persistent storage.
     * <p>
     * This method attempts to deserialize the book issue data from the file
     * "./db/bookIssues.ser". If the file doesn't exist or is empty, it initializes
     * an empty list of book issues.
     *
     * @see utils#loadData(String)
     */
    public void loadBookIssued() {
        List<BookIssue> loadedIssues = utils.loadData("./db/bookIssues.ser");
        this.bookIssued = (loadedIssues != null) ? loadedIssues : new ArrayList<>();
    }

    /**
     * Helper method to display a list of book issued.
     */
    /**
     * Displays a formatted list of book issues in a tabular format.
     * <p>
     * The table includes columns for book title, author, member name, member type,
     * issue date, and due date. The column widths are automatically adjusted
     * based on the content.
     *
     * @param issuedList the list of book issues to display (can be empty)
     * @see BookIssue
     * @see utils#convertEpochToDate(long)
     */
    private void showIssueList(List<BookIssue> issuedList) {
        System.out.println("Result -\n");
        if (issuedList.size() == 0) {
            System.out.println("Error: No book issues found");
            System.out.println("=====================================");
            return;
        }

        String[] headers = { "BOOK TITLE", "AUTHOR", "ISSUED BY", "TYPE", "ISSUED DATE", "DUE DATE" };

        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (BookIssue issuedBook : issuedList) {

            colWidths[0] = Math.max(colWidths[0], issuedBook.getBook().getTitle().length());
            colWidths[1] = Math.max(colWidths[1], issuedBook.getBook().getAuthor().length());
            colWidths[2] = Math.max(colWidths[2], issuedBook.getMember().getName().length());
            colWidths[3] = Math.max(colWidths[3], issuedBook.getMember().getMemberType().length());
            colWidths[4] = Math.max(colWidths[4], String.valueOf(issuedBook.getIssueDate()).length());
            colWidths[5] = Math.max(colWidths[5], String.valueOf(issuedBook.getDueDate()).length());

        }

        // Build format string
        String format = String.format(
                "%%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds%n",
                colWidths[0], colWidths[1], colWidths[2], colWidths[3], colWidths[4], colWidths[5]);

        // Print header
        System.out.printf(format, (Object[]) headers);
        System.out.println();

        // Print rows
        for (BookIssue issue : issuedList) {
            System.out.printf(format,
                    issue.getBook().getTitle(),
                    issue.getBook().getAuthor(),
                    issue.getMember().getName(),
                    issue.getMember().getMemberType(),
                    utils.convertEpochToDate(issue.getIssueDate()),
                    utils.convertEpochToDate(issue.getDueDate()));
        }
        System.out.println("\n===================================== END ISSUE LIST =============================\n");
    }

    /**
     * Retrieves the details of a specific book issue.
     *
     * @param memberId the ID of the member who borrowed the book
     * @param bookId   the ISBN of the borrowed book
     * @return the BookIssue object containing issue details, or null if not found
     * @throws IllegalArgumentException if memberId or bookId is invalid
     * @see BookIssue
     */
    public BookIssue getBookIssueDetail(long memberId, long bookId) throws Exception {
        if (memberId <= 0 || bookId <= 0) {
            throw new IllegalArgumentException("Member ID and Book ID must be positive numbers");
        }
        int bookIssueIndex = IntStream.range(0, this.bookIssued.size())
                .filter(i -> this.bookIssued.get(i).getMember().getMemberId() == memberId
                        && this.bookIssued.get(i).getBook().getISBN() == bookId)
                .findFirst().orElse(-1);
        if (bookIssueIndex == -1) {
            return null;
        }
        return this.bookIssued.get(bookIssueIndex);
    }

    /**
     * Issues a book to a member
     * 
     * @throws Exception if any error occurs
     */
    /**
     * Issues a book to a member after performing necessary validations.
     * 
     * @throws Exception if the book cannot be issued due to various validation
     *                   failures
     */
    public void issueBook() throws Exception {
        // Step 1: Get and validate member information
        System.out.print("Enter member ID: ");
        long memberId = Long.parseLong(scn.nextLine());
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            throw new Exception("Member not found");
        }
        if (!member.canBorrowBooks()) {
            throw new Exception("This member can't able to borrow books");
        }

        // Step 2: Get and validate book information
        System.out.print("Enter book ID: ");
        long bookId = Long.parseLong(scn.nextLine());
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

        // Step 3: Check for existing reservations
        Reservation reservation = this.reservationService.getFirstReservationByBookId(bookId);
        boolean isReserved = false;
        if (reservation != null) {
            // Only allow the reserving member to borrow the book
            if (reservation.getMemberId() != memberId) {
                throw new Exception("This book is reserved by another member");
            }
            // Remove the reservation as it's being fulfilled
            this.reservationService.removeReservation(memberId, bookId);
            this.reservationService.updateReservationInDatabase();
            isReserved = true;
        }

        // Step 4: Check if the book is already issued to the member
        BookIssue issueDetail = getBookIssueDetail(memberId, bookId);
        if (issueDetail != null && issueDetail.getReturnDate() == 0) {
            throw new Exception("This book is already issued by you");
        }

        // Step 5: Create and record the new book issue
        BookIssue bookIssue = new BookIssue(member, book, 0);
        this.bookIssued.add(bookIssue);
        this.updateBookIssueInDatabase();

        // Step 6: Update book availability
        if (book instanceof PhysicalBook) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            physicalBook.setAvailableCopies(physicalBook.getAvailableCopies() - 1);
            if (isReserved) {
                physicalBook.setReservedCopies(physicalBook.getReservedCopies() - 1);
            }
        }
        bookService.updateBookInList(book);
        bookService.updateBookInDatabase();

        // Step 7: Update member's borrowing status
        member.borrowBook();
        memberService.updateMemberInList(member);
        memberService.updateMemberInDatabase();
    }

    /**
     * Returns a book to the library
     * 
     * @throws Exception if any error occurs
     */
    /**
     * Processes the return of a borrowed book, updating all related records.
     * 
     * @throws Exception if the book cannot be returned due to various validation
     *                   failures
     */
    public void returnBook() throws Exception {
        // Step 1: Get and validate member information
        System.out.print("Enter member ID: ");
        long memberId = Long.parseLong(scn.nextLine());
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            throw new Exception("Member not found");
        }
        if (!member.canBorrowBooks()) {
            throw new Exception("This member can't able to return the books");
        }

        // Step 2: Get and validate book information
        System.out.print("Enter book ID: ");
        long bookId = Long.parseLong(scn.nextLine());
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

        // Step 3: Verify the book is actually issued to this member
        BookIssue bookIssue = getBookIssueDetail(memberId, bookId);
        if (bookIssue == null || bookIssue.getReturnDate() != 0) {
            throw new Exception("This book is already returned");
        }

        // Step 4: Update the book issue record with return details
        bookIssue.setReturnDate(utils.getEpochTime());
        int daysOverdue = bookIssue.getDaysOverdue();
        double fineAmount = member.calculateFine(daysOverdue);
        bookIssue.setFineAmount(bookIssue.getFineAmount() + fineAmount);
        this.updateBookIssueInDatabase();

        // Step 5: Update book availability
        if (book instanceof PhysicalBook) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            physicalBook.setAvailableCopies(physicalBook.getAvailableCopies() + 1);
        }
        bookService.updateBookInList(book);
        bookService.updateBookInDatabase();

        // Step 6: Update member's borrowing status
        member.returnBook();
        memberService.updateMemberInList(member);
        memberService.updateMemberInDatabase();
    }

    /**
     * Renews a book for a member
     * 
     * @throws Exception if any error occurs
     */
    /**
     * Renews the due date for a borrowed book if the member is eligible.
     * 
     * @throws Exception if the book cannot be renewed due to various validation
     *                   failures
     */
    public void renewBook() throws Exception {
        // Step 1: Get and validate member information
        System.out.print("Enter member ID: ");
        long memberId = Long.parseLong(scn.nextLine());
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            throw new Exception("Member not found");
        }
        if (!member.canRenewBooks()) {
            throw new Exception("This member can't renew the books at this time");
        }

        // Step 2: Get and validate book information
        System.out.print("Enter book ID: ");
        long bookId = Long.parseLong(scn.nextLine());
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new Exception("Book not found");
        }
        if (book instanceof PhysicalBook) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            if (!physicalBook.isAvailable()) {
                throw new Exception("Book is not available for renewal");
            }
        }

        // Step 3: Check for existing reservations
        Reservation reservation = this.reservationService.getFirstReservationByBookId(bookId);
        boolean isReserved = false;
        if (reservation != null) {
            // Prevent renewal if someone else has reserved the book
            if (reservation.getMemberId() != memberId) {
                throw new Exception("This book is reserved by another member. You can't renew it now.");
            }
            // If the reservation is by the same member, remove it
            this.reservationService.removeReservation(memberId, bookId);
            this.reservationService.updateReservationInDatabase();
            isReserved = true;
        }

        // Step 4: Find the existing book issue record
        int bookIssueIndex = IntStream.range(0, this.bookIssued.size())
                .filter(i -> this.bookIssued.get(i).getMember().getMemberId() == memberId
                        && this.bookIssued.get(i).getBook().getISBN() == bookId)
                .findFirst().orElse(-1);
        if (bookIssueIndex == -1) {
            throw new Exception("No active book issue found for this member and book");
        }

        // Step 5: Update the due date for the book issue
        BookIssue bookIssue = this.bookIssued.get(bookIssueIndex);
        bookIssue.setDueDate(utils.getDateAfterDays(bookIssue.getDueDate(), MemberPolicy.defaultDueDate()));
        this.bookIssued.set(bookIssueIndex, bookIssue);
        this.updateBookIssueInDatabase();

        // Step 6: Update member's renewal status
        member.renewBook();
        memberService.updateMemberInList(member);
        memberService.updateMemberInDatabase();

        // Step 7: Update book status if it was reserved
        if (book instanceof PhysicalBook && isReserved) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            physicalBook.setReservedCopies(physicalBook.getReservedCopies() - 1);
            bookService.updateBookInList(book);
            bookService.updateBookInDatabase();
        }
    }

    /**
     * Saves the current list of book issues to persistent storage.
     * <p>
     * This method serializes the current state of book issues and saves it to
     * "./db/bookIssues.txt". It should be called after any modifications to
     * ensure data persistence.
     *
     * @throws Exception if an I/O error occurs during the save operation
     * @see utils#saveData(String, Object)
     */
    public void updateBookIssueInDatabase() throws Exception {
        try {
            utils.saveData("./db/bookIssues.ser", this.bookIssued);
        } catch (Exception e) {
            throw new Exception("Failed to save book issues: " + e.getMessage(), e);
        }
    }

    /**
     * /**
     * Displays a list of books that are overdue (not returned and past their due
     * date)
     * 
     * @throws Exception if there's an error accessing the book issue data
     */

    /**
     * Displays a list of all overdue books that haven't been returned.
     * <p>
     * This method finds all book issues where the return date is not set (0)
     * and the due date has passed the current date.
     *
     * @throws Exception if there's an error accessing the book issue data
     * @see #showIssueList(List)
     * @see utils#getEpochTime()
     */
    public void viewOverDueBooks() throws Exception {
        List<BookIssue> overDueBooks = new ArrayList<>();
        for (BookIssue issue : this.bookIssued) {
            if (issue.getReturnDate() == 0 && issue.getDueDate() < utils.getEpochTime()) {
                overDueBooks.add(issue);
            }
        }
        showIssueList(overDueBooks);
    }

    /**
     * Generates and displays book recommendations based on popular authors.
     * <p>
     * This method analyzes the most frequently borrowed authors and recommends
     * other books by those authors. It shows the top 10 most popular authors
     * and their available books.
     *
     * @throws Exception if there's an error accessing book or issue data
     * @see BookService#getBooksByAuthor(String)
     * @see BookService#showBookList(List)
     */
    public void bookRecommendations() throws Exception {
        System.out.print("Enter member ID: ");
        long memberId = Long.parseLong(scn.nextLine());
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            throw new Exception("Member not found");
        }

        // book issued count by book author
        List<Book> recommendedBooks = new ArrayList<>();

        // get top 5 authors
        List<String> topAuthors = this.bookIssued.stream().filter(issue -> issue.getMember().getMemberId() == memberId)
                .collect(Collectors.groupingBy(issue -> issue.getBook().getAuthor(), Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(10).map(Map.Entry::getKey).collect(Collectors.toList());

        for (String author : topAuthors) {
            recommendedBooks.addAll(this.bookService.getBooksByAuthor(author));
        }
        // show recommended books
        this.bookService.showBookList(recommendedBooks);
    }

    /**
     * Displays a formatted list of popular books with their issue and reservation
     * counts.
     * <p>
     * The table includes columns for book title, number of times issued, and
     * number of times reserved. The column widths are automatically adjusted
     * based on the content.
     *
     * @param topBooks a list of maps containing book popularity data
     * @see #getPopularBooks()
     */
    protected void showPopularBooks(List<Map<String, Object>> topBooks) {
        System.out.println("Result -\n");
        if (topBooks.size() == 0) {
            System.out.println("Error: No books found");
            System.out.println("=====================================");
            return;
        }

        String[] headers = { "TITLE", "ISSUED COUNT", "RESERVED COUNT" };

        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (Map<String, Object> book : topBooks) {
            colWidths[0] = Math.max(colWidths[0], book.get("Title").toString().length());
            colWidths[1] = Math.max(colWidths[1], String.valueOf(book.get("issuedCount")).length());
            colWidths[2] = Math.max(colWidths[2], String.valueOf(book.get("reservedCount")).length());
        }

        // Build format string
        String format = String.format(
                "%%-%ds  %%-%ds  %%-%ds%n",
                colWidths[0], colWidths[1], colWidths[2]);

        // Print header
        System.out.printf(format, (Object[]) headers);
        System.out.println();

        // Print rows
        for (Map<String, Object> book : topBooks) {
            System.out.printf(format, book.get("Title"), book.get("issuedCount"), book.get("reservedCount"));
        }
        System.out.println("=====================================");
    }

    /**
     * Retrieves and displays the most popular books in the library.
     * <p>
     * This method identifies the top 5 most borrowed books based on issue history
     * and displays them along with their issue counts and reservation counts.
     *
     * @throws Exception if there's an error accessing book or issue data
     * @see #showPopularBooks(List)
     * @see BookService#getBookById(long)
     */
    public void getPopularBooks() throws Exception {
        // book issued count by book author
        List<Book> popularBooks = new ArrayList<>();

        // get top 5 books with title and issuedCount
        List<Map<String, Object>> topBooks = this.bookIssued.stream()
                .collect(Collectors.groupingBy(issue -> issue.getBook().getISBN(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("Title", this.bookService.getBookById(entry.getKey()).getTitle());
                    map.put("issuedCount", entry.getValue());
                    map.put("reservedCount",
                            ((PhysicalBook) this.bookService.getBookById(entry.getKey())).getReservedCopies());
                    return map;
                })
                .collect(Collectors.toList());

        // show recommended books
        showPopularBooks(topBooks);
    }

    /**
     * Provides an advanced search interface for various book-related queries.
     * <p>
     * This method presents a menu of advanced search options:
     * 1. Search books by multiple authors
     * 2. Filter available books by category and publication year
     * 3. View members with overdue books
     *
     * @throws Exception if there's an error during the search operation
     * @see #membersWithOverDueBooks()
     * @see BookService#searchBookByMultipleAuthor()
     * @see BookService#filterAvailableBooksByCategoryAndPublicationYear()
     */
    public void advancedSearch() throws Exception {
        System.out.println("1.Search Book by multiple Author - (use comma to separate authors)");
        System.out.println("2.Filter available books by category and publication year");
        System.out.println("3.Members with overdue books");
        String value = scn.nextLine().split("\\s+")[0];
        switch (value) {
            case "1":
                bookService.searchBookByMultipleAuthor();
                break;
            case "2":
                bookService.filterAvailableBooksByCategoryAndPublicationYear();
                break;
            case "3":
                membersWithOverDueBooks();
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    /**
     * Displays a list of members who currently have overdue books.
     * <p>
     * This method finds all book issues where the return date is not set (0)
     * and the due date has passed the current date, then groups them by member.
     *
     * @throws Exception if there's an error accessing the book issue data
     * @see #showIssueList(List)
     * @see utils#getEpochTime()
     */
    public void membersWithOverDueBooks() throws Exception {
        List<BookIssue> members = new ArrayList<>();
        for (BookIssue issue : this.bookIssued) {
            if (issue.getReturnDate() == 0 && issue.getDueDate() < utils.getEpochTime()) {
                members.add(issue);
            }
        }
        showIssueList(members);
    }

    /**
     * Generates a monthly borrowing report.
     * <p>
     * This method analyzes book issues by month and displays the count of
     * books borrowed each month. The report helps in understanding borrowing
     * patterns and trends over time.
     *
     * @throws Exception if there's an error processing the issue data
     * @see YearMonth
     * @see utils#convertEpochToDate(long)
     */
    public void borrowingReports() throws Exception {
        Map<YearMonth, Long> monthlyBorrows = this.bookIssued.stream()
                .collect(Collectors.groupingBy(
                        issue -> YearMonth.from(utils.convertEpochToDate(issue.getIssueDate())),
                        Collectors.counting()));
        System.out.println(monthlyBorrows);
    }

    /**
     * Calculates and displays the total fines collected from all book issues.
     * <p>
     * This method sums up all fine amounts from book issues where fines were
     * applied. It provides insight into the library's fine revenue.
     *
     * @throws Exception if there's an error accessing the book issue data
     * @see BookIssue#getFineAmount()
     */
    public void fineCollectionReports() throws Exception {
        double totalFines = this.bookIssued.stream()
                .mapToDouble(issue -> issue.getFineAmount())
                .sum();
        System.out.println(totalFines);
    }

    /**
     * Replaces the list of book issues with the given list
     * 
     * @param bookIssued the new list of book issues
     */
    /**
     * Replaces the current list of book issues with a new list.
     * <p>
     * This method is primarily used for loading book issues from persistent storage
     * or for testing purposes. It completely replaces the existing list of book
     * issues.
     *
     * @param bookIssued the new list of book issues to use
     * @throws IllegalArgumentException if the provided list is null
     */
    protected void replaceBookIssueList(List<BookIssue> bookIssued) {
        if (bookIssued == null) {
            throw new IllegalArgumentException("Book issues list cannot be null");
        }
        this.bookIssued = bookIssued;
    }
}