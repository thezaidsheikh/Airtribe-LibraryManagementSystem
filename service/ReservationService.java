package service;

import java.util.List;
import java.util.Scanner;

import common.utils;
import model.Book;
import model.Member;
import model.PhysicalBook;
import model.Reservation;

public class ReservationService {
    Scanner scn = new Scanner(System.in);
    MemberService memberService;
    BookService bookService;
    List<Reservation> reservations;

    public ReservationService(MemberService memberService, BookService bookService) {
        this.memberService = memberService;
        this.bookService = bookService;
    }

    protected boolean isAlreadyReservedByMemberIdAndBookId(long memberId, long bookId) {
        for (Reservation reservation : this.reservations) {
            if (reservation.getMemberId() == memberId && reservation.getBookId() == bookId) {
                return true;
            }
        }
        return false;
    }

    protected void removeReservation(long memberId, long bookId) {
        this.reservations
                .removeIf(reservation -> reservation.getMemberId() == memberId && reservation.getBookId() == bookId);
    }

    protected void updateReservationToDatabase() throws Exception {
        utils.saveData("./db/reservations.txt", this.reservations);
    }

    protected Reservation getFirstReservationByBookId(long bookId) {
        for (Reservation reservation : this.reservations) {
            if (reservation.getBookId() == bookId) {
                return reservation;
            }
        }
        return null;
    }

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

        utils.saveData("./db/reservations.txt", this.reservations);
    }
}
