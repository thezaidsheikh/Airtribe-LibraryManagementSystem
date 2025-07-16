package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import common.MemberPolicy;
import common.utils;
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
    List<BookIssue> bookIssues = new ArrayList<>();

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

    public void loadBookIssues() {
        this.bookIssues = utils.loadData("./db/bookIssues.ser");
    }

    public BookIssue getBookIssueDetail(long memberId, long bookId) throws Exception {
        int bookIssueIndex = IntStream.range(0, this.bookIssues.size())
                .filter(i -> this.bookIssues.get(i).getMember().getMemberId() == memberId
                        && this.bookIssues.get(i).getBook().getISBN() == bookId)
                .findFirst().orElse(-1);
        if (bookIssueIndex == -1) {
            return null;
        }
        return this.bookIssues.get(bookIssueIndex);
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
        boolean isReserved = false;
        if (reservation != null) {
            if (reservation.getMemberId() != memberId) {
                throw new Exception("This book is reserved by another member");
            }
            this.reservationService.removeReservation(memberId, bookId);
            this.reservationService.updateReservationInDatabase();
            isReserved = true;
        }

        BookIssue issueDetail = getBookIssueDetail(memberId, bookId);
        if (issueDetail != null && issueDetail.getReturnDate() == 0) {
            throw new Exception("This book is already issued by you");
        }

        BookIssue bookIssue = new BookIssue(member, book, 0);
        this.bookIssues.add(bookIssue);

        this.updateBookIssueInDatabase();

        // update book
        if (book instanceof PhysicalBook) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            physicalBook.setAvailableCopies(physicalBook.getAvailableCopies() - 1);
            if (isReserved) {
                physicalBook.setReservedCopies(physicalBook.getReservedCopies() - 1);
            }
        }
        bookService.updateBookInList(book);
        bookService.updateBookInDatabase();
        // update member
        member.borrowBook();
        memberService.updateMemberInList(member);
        memberService.updateMemberInDatabase();
    }

    /**
     * Returns a book to the library
     * 
     * @throws Exception if any error occurs
     */
    public void returnBook() throws Exception {
        System.out.print("Enter member ID: ");
        long memberId = Long.parseLong(scn.nextLine());
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            throw new Exception("Member not found");
        }
        if (!member.canBorrowBooks()) {
            throw new Exception("This member can't able to return the books");
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

        BookIssue bookIssue = getBookIssueDetail(memberId, bookId);
        if (bookIssue == null || bookIssue.getReturnDate() != 0) {
            throw new Exception("This book is already returned");
        }

        bookIssue.setReturnDate(utils.getEpochTime());
        int daysOverdue = bookIssue.getDaysOverdue();
        member.calculateFine(daysOverdue);
        this.updateBookIssueInDatabase();

        // update book
        if (book instanceof PhysicalBook) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            physicalBook.setAvailableCopies(physicalBook.getAvailableCopies() + 1);
        }
        bookService.updateBookInList(book);
        bookService.updateBookInDatabase();

        // update member
        member.returnBook();
        memberService.updateMemberInList(member);
        memberService.updateMemberInDatabase();
    }

    /**
     * Renews a book for a member
     * 
     * @throws Exception if any error occurs
     */
    public void renewBook() throws Exception {
        System.out.print("Enter member ID: ");
        long memberId = Long.parseLong(scn.nextLine());
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            throw new Exception("Member not found");
        }
        if (!member.canRenewBooks()) {
            throw new Exception("This member can't able to renew the books");
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
        boolean isReserved = false;
        if (reservation != null) {
            if (reservation.getMemberId() != memberId) {
                throw new Exception("This book is reserved by another member. You can't renew it now.");
            }
            this.reservationService.removeReservation(memberId, bookId);
            this.reservationService.updateReservationInDatabase();
            isReserved = true;
        }

        int bookIssueIndex = IntStream.range(0, this.bookIssues.size())
                .filter(i -> this.bookIssues.get(i).getMember().getMemberId() == memberId
                        && this.bookIssues.get(i).getBook().getISBN() == bookId)
                .findFirst().orElse(-1);
        if (bookIssueIndex == -1) {
            throw new Exception("Book issue not found");
        }

        BookIssue bookIssue = this.bookIssues.get(bookIssueIndex);
        bookIssue.setDueDate(utils.getDateAfterDays(bookIssue.getDueDate(), MemberPolicy.defaultDueDate()));
        this.bookIssues.set(bookIssueIndex, bookIssue);
        this.updateBookIssueInDatabase();

        member.renewBook();
        memberService.updateMemberInList(member);
        memberService.updateMemberInDatabase();
        if (book instanceof PhysicalBook && isReserved) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            physicalBook.setReservedCopies(physicalBook.getReservedCopies() - 1);
        }
        bookService.updateBookInList(book);
        bookService.updateBookInDatabase();
    }

    public void updateBookIssueInDatabase() throws Exception {
        utils.saveData("./db/bookIssues.txt", this.bookIssues);
    }

    public void viewOverDueBooks() throws Exception {
        this.bookIssues.stream()
                .filter(bookIssue -> bookIssue.getReturnDate() == 0 && bookIssue.getDueDate() < utils.getEpochTime())
                .forEach(bookIssue -> System.out.println(bookIssue));
    }
}
