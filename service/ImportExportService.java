package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import common.BookCategory;
import common.MemberStatus;
import common.utils;
import model.AudioBook;
import model.Book;
import model.BookIssue;
import model.EBook;
import model.FacultyMember;
import model.Member;
import model.PhysicalBook;
import model.RegularMember;
import model.StudentMember;

public class ImportExportService {
    Scanner scn = new Scanner(System.in);

    private MemberService memberService;
    private BookService bookService;
    private BookIssueService bookIssueService;

    public ImportExportService(MemberService memberService, BookService bookService,
            BookIssueService bookIssueService) {
        this.memberService = memberService;
        this.bookService = bookService;
        this.bookIssueService = bookIssueService;
    }

    private static Map<String, String> parseLine(String line) {
        Map<String, String> result = new HashMap<>();
        String[] keyValuePairs = line.split(",\\s*");

        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=", 2);
            if (entry.length == 2) {
                result.put(entry[0].trim(), entry[1].trim());
            }
        }

        return result;
    }

    /**
     * Imports data from a file
     * 
     * @param fileName the name of the file to import from
     * @throws Exception if an error occurs during import
     */
    public void importData() throws Exception {
        System.out.println("Choose what to import:");
        System.out.println("1. Books");
        System.out.println("2. Members");
        String choice = scn.nextLine();
        switch (choice) {
            case "1":
                importBooks();
                break;
            case "2":
                importMembers();
                break;
            case "3":
                importBookIssued();
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    /**
     * Imports books from a file
     * 
     * @throws Exception if an error occurs during import
     */
    private void importBooks() throws Exception {
        System.out.println("====================== START - Importing books ======================");
        System.out.println("Enter the file name to import from:");
        String fileName = scn.nextLine();

        // Import books from file
        System.out.println("Importing books from file: " + fileName);
        String filePath = "./import/" + fileName;

        // Read data from file
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found");
            return;
        }

        // Import books from file
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<Book> books = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            Map<String, String> book = parseLine(line);

            // Create book object
            Book b = null;
            if (book.get("bookType").equals("Physical Book")) {
                b = new PhysicalBook(book.get("title"), book.get("author"), book.get("publisher"),
                        Integer.parseInt(book.get("publicationYear")),
                        BookCategory.getCategoryByName(book.get("category")), Integer.parseInt(book.get("pages")),
                        Integer.parseInt(book.get("totalCopies")), Integer.parseInt(book.get("availableCopies")),
                        Integer.parseInt(book.get("reservedCopies")));
            } else if (book.get("bookType").equals("Audio Book")) {
                b = new AudioBook(book.get("title"), book.get("author"), book.get("publisher"),
                        Integer.parseInt(book.get("publicationYear")),
                        BookCategory.getCategoryByName(book.get("category")), book.get("narratorName"),
                        book.get("audioFormat"), Integer.parseInt(book.get("audioLength")));
            } else if (book.get("bookType").equals("EBook")) {
                b = new EBook(book.get("title"), book.get("author"), book.get("publisher"),
                        Integer.parseInt(book.get("publicationYear")),
                        BookCategory.getCategoryByName(book.get("category")), book.get("fileFormat"),
                        Boolean.parseBoolean(book.get("drmProtected")));
            }
            books.add(b);

        }
        if (books.size() > 0) {
            utils.saveData("./db/books.txt", books);
            this.bookService.replaceBookList(books);
        }
        System.out.println("====================== END - Importing books ========================");
    }

    /**
     * Imports members from a file
     * 
     * @throws Exception
     */
    private void importMembers() throws Exception {
        System.out.println("====================== START - Importing Members ========================");
        System.out.println("Enter the file name to import from:");
        String fileName = scn.nextLine();

        // Import members from file
        System.out.println("Importing members from file: " + fileName);
        String filePath = "import/" + fileName;
        // Read data from file
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found");
            return;
        }

        // Import books from file
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<Member> members = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            Map<String, String> member = parseLine(line);

            // Create book object
            Member m = null;
            if (member.get("memberType").equals("Student")) {
                m = new StudentMember(member.get("name"), member.get("email"),
                        Long.parseLong(member.get("phone")), member.get("studentId"), member.get("academicYear"),
                        member.get("department"), MemberStatus.getStatusByName(member.get("membershipStatus")),
                        Long.parseLong(member.get("membershipDate")),
                        Integer.parseInt(member.get("currentBorrowedBooks")),
                        Double.parseDouble(member.get("totalFineAmount")),
                        Integer.parseInt(member.get("renewalCount")));
            } else if (member.get("memberType").equals("Faculty")) {
                m = new FacultyMember(member.get("name"), member.get("email"),
                        Long.parseLong(member.get("phone")), member.get("facultyId"), member.get("department"),
                        member.get("designation"), MemberStatus.getStatusByName(member.get("membershipStatus")),
                        Long.parseLong(member.get("membershipDate")),
                        Integer.parseInt(member.get("currentBorrowedBooks")),
                        Double.parseDouble(member.get("totalFineAmount")),
                        Integer.parseInt(member.get("renewalCount")));
            } else if (member.get("memberType").equals("Regular")) {
                m = new RegularMember(member.get("name"), member.get("email"),
                        Long.parseLong(member.get("phone")),
                        MemberStatus.getStatusByName(member.get("membershipStatus")),
                        Long.parseLong(member.get("membershipDate")),
                        Integer.parseInt(member.get("currentBorrowedBooks")),
                        Double.parseDouble(member.get("totalFineAmount")),
                        Integer.parseInt(member.get("renewalCount")));
            }
            members.add(m);

        }
        if (members.size() > 0) {
            utils.saveData("./db/members.txt", members);
            this.memberService.replaceMemberList(members);
        }
        System.out.println("====================== END - Importing Members ========================");
    }

    /**
     * Imports book issued data from a file
     * 
     * @throws Exception if an error occurs during import
     */
    private void importBookIssued() throws Exception {
        System.out.println("====================== START - Importing Book Issued ========================");
        System.out.println("Enter the file name to import from:");
        String fileName = scn.nextLine();

        // Import book issued data from file
        System.out.println("Importing book issued data from file: " + fileName);
        String filePath = "import/" + fileName;
        // Read data from file
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found");
            return;
        }

        // Import book issued data from file
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<BookIssue> bookIssued = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            Map<String, String> bookIssuedData = parseLine(line);
            long memberId = Long.parseLong(bookIssuedData.get("memberId"));
            long bookId = Long.parseLong(bookIssuedData.get("bookId"));
            Member member = this.memberService.getMemberById(memberId);
            Book book = this.bookService.getBookById(bookId);

            // Create book issued object
            BookIssue b = new BookIssue(member, book, Double.parseDouble(bookIssuedData.get("fineAmount")),
                    Long.parseLong(bookIssuedData.get("issueDate")), Long.parseLong(bookIssuedData.get("dueDate")),
                    Long.parseLong(bookIssuedData.get("returnDate")));
            bookIssued.add(b);
        }
        if (bookIssued.size() > 0) {
            utils.saveData("./db/bookIssues.txt", bookIssued);
            this.bookIssueService.replaceBookIssueList(bookIssued);
        }
        System.out.println("====================== END - Importing Book Issued ========================");
    }
}
