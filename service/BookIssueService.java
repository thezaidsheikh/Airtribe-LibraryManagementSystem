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

public class BookIssueService {
    Scanner scn = new Scanner(System.in);
    MemberService memberService;
    BookService bookService;
    ReservationService reservationService;
    List<BookIssue> bookIssued = new ArrayList<>();

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

    public void loadBookIssued() {
        this.bookIssued = utils.loadData("./db/bookIssues.ser");
    }

    /**
     * Helper method to display a list of book issued.
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

    public BookIssue getBookIssueDetail(long memberId, long bookId) throws Exception {
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
        this.bookIssued.add(bookIssue);

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
        double fineAmount = member.calculateFine(daysOverdue);
        bookIssue.setFineAmount(bookIssue.getFineAmount() + fineAmount);
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

        int bookIssueIndex = IntStream.range(0, this.bookIssued.size())
                .filter(i -> this.bookIssued.get(i).getMember().getMemberId() == memberId
                        && this.bookIssued.get(i).getBook().getISBN() == bookId)
                .findFirst().orElse(-1);
        if (bookIssueIndex == -1) {
            throw new Exception("Book issue not found");
        }

        BookIssue bookIssue = this.bookIssued.get(bookIssueIndex);
        bookIssue.setDueDate(utils.getDateAfterDays(bookIssue.getDueDate(), MemberPolicy.defaultDueDate()));
        this.bookIssued.set(bookIssueIndex, bookIssue);
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
        utils.saveData("./db/bookIssues.txt", this.bookIssued);
    }

    /**
     * /**
     * Displays a list of books that are overdue (not returned and past their due
     * date)
     * 
     * @throws Exception if there's an error accessing the book issue data
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

    public void bookRecommendations() throws Exception {
        // book issued count by book author
        List<Book> recommendedBooks = new ArrayList<>();

        // get top 5 authors
        List<String> topAuthors = this.bookIssued.stream()
                .collect(Collectors.groupingBy(issue -> issue.getBook().getAuthor(), Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(10).map(Map.Entry::getKey).collect(Collectors.toList());

        for (String author : topAuthors) {
            recommendedBooks.addAll(this.bookService.getBooksByAuthor(author));
        }
        // show recommended books
        this.bookService.showBookList(recommendedBooks);
    }

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

    public void membersWithOverDueBooks() throws Exception {
        List<BookIssue> members = new ArrayList<>();
        for (BookIssue issue : this.bookIssued) {
            if (issue.getReturnDate() == 0 && issue.getDueDate() < utils.getEpochTime()) {
                members.add(issue);
            }
        }
        showIssueList(members);
    }

    public void borrowingReports() throws Exception {
        Map<YearMonth, Long> monthlyBorrows = this.bookIssued.stream()
                .collect(Collectors.groupingBy(
                        issue -> YearMonth.from(utils.convertEpochToDate(issue.getIssueDate())),
                        Collectors.counting()));
        System.out.println(monthlyBorrows);
    }

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
    protected void replaceBookIssueList(List<BookIssue> bookIssued) {
        this.bookIssued = bookIssued;
    }
}