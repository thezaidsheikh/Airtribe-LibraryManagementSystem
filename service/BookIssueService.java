package service;

import java.util.List;
import java.util.Scanner;

import model.Book;
import model.BookIssue;
import model.Member;
import model.PhysicalBook;
import model.Reservation;

public class BookIssueService {
    Scanner scn = new Scanner(System.in);
    MemberService memberService;
    BookService bookService;
    ReservationService reservationService;
    List<BookIssue> bookIssues;

    /**
     * Constructor to initialize the memberService and bookService
     * 
     * @param memberService
     * @param bookService
     */
    public BookIssueService(MemberService memberService, BookService bookService,
            ReservationService reservationService) {
        this.memberService = memberService;
        this.bookService = bookService;
        this.reservationService = reservationService;
    }

    /**
     * Issues a book to a member
     * 
     * @throws Exception if any error occurs
     */
    public void issueBook() throws Exception {
        System.out.print("Enter member ID: ");
        long memberId = Long.parseLong(scn.nextLine());
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            throw new Exception("Member not found");
        }
        if (!member.canBorrowBooks()) {
            throw new Exception("This member can't able to borrow books");
        }

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
        Reservation reservation = this.reservationService.getFirstReservationByBookId(bookId);
        if (reservation != null) {
            if (reservation.getMemberId() != memberId) {
                throw new Exception("This book is reserved by another member");
            }
            this.reservationService.removeReservation(memberId, bookId);
        }

        BookIssue bookIssue = new BookIssue(member, book, 0);
        this.bookIssues.add(bookIssue);
        bookService.updateBookInList(book);
        bookService.updateBookInDatabase();
        memberService.updateMemberAndSaveDatabase(member);
    }

}
