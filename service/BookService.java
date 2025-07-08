package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.BookCategory;
import common.utils;
import model.AudioBook;
import model.Book;
import model.EBook;
import model.PhysicalBook;

public class BookService {
    Scanner scn = new Scanner(System.in);
    List<Book> books = new ArrayList<>();

    public void loadBooks() {
        this.books = utils.loadData("./db/books.ser");
    }

    // Add a new book
    public void addBook() throws Exception {
        System.out.print("Enter the book title. (mandatory - max 30 characters): ");
        String title = scn.nextLine();
        if (title.isEmpty() || title.length() > 30) {
            throw new Exception("Invalid book title");
        }

        System.out.print("Choose the book category (mandatory): ");
        System.out.println(
                "1. Fiction\n2. Non-Fiction\n3. Science\n4. Technology\n5. History\n6. Biography\n7. Self-Help\n8. Children\n9. Poetry\n10. Drama");
        int category = Integer.parseInt(scn.nextLine());
        if (category < 1 || category > 10 || BookCategory.getCategory(category) == null) {
            throw new Exception("Invalid book category");
        }
        BookCategory bookCategory = BookCategory.getCategory(category);
        System.out.println("Book category: " + bookCategory);

        System.out.print("Enter the book author. (mandatory - max 30 characters): ");
        String author = scn.nextLine();
        if (author.isEmpty() || author.length() > 30) {
            throw new Exception("Invalid book author");
        }

        System.out.print("Enter the book publisher. (mandatory - max 30 characters): ");
        String publisher = scn.nextLine();
        if (publisher.isEmpty() || publisher.length() > 30) {
            throw new Exception("Invalid book publisher");
        }

        System.out.print("Enter the book publication year. (mandatory): ");
        int publicationYear = Integer.parseInt(scn.nextLine());
        if (publicationYear < 0) {
            throw new Exception("Invalid book publication year");
        }

        System.out.println("Choose the type of book (mandatory): ");
        System.out.println("1. Physical Book\n2. E-Book\n3. Audio Book");
        int type = Integer.parseInt(scn.nextLine());
        if (type < 1 || type > 3) {
            throw new Exception("Invalid book type");
        }

        if (type == 1) {
            System.out.print("Enter the book pages. (mandatory): ");
            int pages = Integer.parseInt(scn.nextLine());
            if (pages < 0) {
                throw new Exception("Invalid book pages");
            }
            System.out.print("Enter the book total copies. (mandatory): ");
            int totalCopies = Integer.parseInt(scn.nextLine());
            if (totalCopies < 0) {
                throw new Exception("Invalid book total copies");
            }

            PhysicalBook physicalBook = new PhysicalBook(title, author, publisher, publicationYear, pages, totalCopies,
                    bookCategory);
            this.books.add(physicalBook);
        }

        if (type == 2) {
            System.out.print("Enter the file format. (mandatory): ");
            String fileFormat = scn.nextLine();
            if (fileFormat.isEmpty()) {
                throw new Exception("Invalid book file format");
            }

            System.out.println("Is the book drm protected? (mandatory - true/false): ");
            boolean drmProtected = Boolean.parseBoolean(scn.nextLine());
            if (drmProtected != true & drmProtected != false) {
                throw new Exception("Invalid book drm protected");
            }

            EBook eBook = new EBook(title, author, publisher, publicationYear, fileFormat, drmProtected);
            this.books.add(eBook);
        }

        if (type == 3) {
            System.out.println("Enter the narrator name. (mandatory): ");
            String narratorName = scn.nextLine();
            if (narratorName.isEmpty()) {
                throw new Exception("Invalid book narrator name");
            }

            System.out.print("Enter the audio format. (mandatory): ");
            String audioFormat = scn.nextLine();
            if (audioFormat.isEmpty()) {
                throw new Exception("Invalid book audio format");
            }

            System.out.println("Enter the audio length in hours. (mandatory): ");
            int audioLength = Integer.parseInt(scn.nextLine());
            if (audioLength < 0) {
                throw new Exception("Invalid book audio length");
            }

            AudioBook audioBook = new AudioBook(title, author, publisher, publicationYear, narratorName, audioFormat,
                    audioLength);
            this.books.add(audioBook);
        }

        utils.saveData("./db/books.txt", this.books);
    }

    // Update a book
    public void updateBook() {
    }

    // Search for a book
    public void searchBook() {
    }

    // View book details
    public void viewBookDetails() {
    }

    // Check book availability
    public void checkBookAvailability() {
    }
}
