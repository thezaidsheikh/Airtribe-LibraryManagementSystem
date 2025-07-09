package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

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

    /**
     * Helper method to display a list of books.
     */
    private void showBookList(List<Book> booksList) {
        System.out.println("Result -\n");
        if (booksList.size() == 0) {
            System.out.println("Error: No books found");
            System.out.println("=====================================");
            return;
        }

        String[] headers = { "ISBN", "TITLE", "AUTHOR", "PUBLISHER", "PUBLICATION YEAR", "CATEGORY", "BOOK TYPE",
                "PAGES", "TOTAL COPIES", "AVAILABLE COPIES", "RESERVED COPIES", "FILE FORMAT", "DRM PROTECTED",
                "NARRATOR NAME", "AUDIO FORMAT", "AUDIO LENGTH" };

        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (Book book : booksList) {

            colWidths[0] = Math.max(colWidths[0], String.valueOf(book.getISBN()).length());
            colWidths[1] = Math.max(colWidths[1], book.getTitle().length());
            colWidths[2] = Math.max(colWidths[2], book.getAuthor().length());
            colWidths[3] = Math.max(colWidths[3], book.getPublisher().length());
            colWidths[4] = Math.max(colWidths[4], String.valueOf(book.getPublicationYear()).length());
            colWidths[5] = Math.max(colWidths[5], book.getCategory().name().length());
            colWidths[6] = Math.max(colWidths[6], book.getBookType().length());
            if (book instanceof PhysicalBook) {
                PhysicalBook physicalBook = (PhysicalBook) book;
                colWidths[7] = Math.max(colWidths[7], String.valueOf(physicalBook.getPages()).length());
                colWidths[8] = Math.max(colWidths[8], String.valueOf(physicalBook.getTotalCopies()).length());
                colWidths[9] = Math.max(colWidths[9], String.valueOf(physicalBook.getAvailableCopies()).length());
                colWidths[10] = Math.max(colWidths[10], String.valueOf(physicalBook.getReservedCopies()).length());
            } else if (book instanceof EBook) {
                EBook eBook = (EBook) book;
                colWidths[11] = Math.max(colWidths[11], String.valueOf(eBook.getFileFormat()).length());
                colWidths[12] = Math.max(colWidths[12], String.valueOf(eBook.isDrmProtected()).length());
            } else if (book instanceof AudioBook) {
                AudioBook audioBook = (AudioBook) book;
                colWidths[13] = Math.max(colWidths[13], String.valueOf(audioBook.getNarratorName()).length());
                colWidths[14] = Math.max(colWidths[14], String.valueOf(audioBook.getAudioFormat()).length());
                colWidths[15] = Math.max(colWidths[15], String.valueOf(audioBook.getAudioLength()).length());
            }
        }

        // Build format string
        String format = String.format(
                "%%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds %%-%ds %%-%ds %%-%ds%n",
                colWidths[0], colWidths[1], colWidths[2], colWidths[3], colWidths[4], colWidths[5], colWidths[6],
                colWidths[7], colWidths[8], colWidths[9], colWidths[10], colWidths[11], colWidths[12], colWidths[13],
                colWidths[14], colWidths[15]);

        // Print header
        System.out.printf(format, (Object[]) headers);
        System.out.println();

        // Print rows
        for (Book book : booksList) {
            if (book instanceof PhysicalBook) {
                PhysicalBook physicalBook = (PhysicalBook) book;
                System.out.printf(format,
                        String.valueOf(physicalBook.getISBN()),
                        physicalBook.getTitle(),
                        physicalBook.getAuthor(),
                        physicalBook.getPublisher(),
                        String.valueOf(physicalBook.getPublicationYear()),
                        physicalBook.getCategory(),
                        physicalBook.getBookType(),
                        String.valueOf(physicalBook.getPages()),
                        String.valueOf(physicalBook.getTotalCopies()),
                        String.valueOf(physicalBook.getAvailableCopies()),
                        String.valueOf(physicalBook.getReservedCopies()),
                        "-", "-", "-", "-", "-");
            } else if (book instanceof EBook) {
                EBook eBook = (EBook) book;
                System.out.printf(format,
                        String.valueOf(eBook.getISBN()),
                        eBook.getTitle(),
                        eBook.getAuthor(),
                        eBook.getPublisher(),
                        String.valueOf(eBook.getPublicationYear()),
                        eBook.getCategory(),
                        eBook.getBookType(),
                        "-", "-", "-", "-",
                        eBook.getFileFormat(),
                        String.valueOf(eBook.isDrmProtected()),
                        "-", "-", "-");
            } else if (book instanceof AudioBook) {
                AudioBook audioBook = (AudioBook) book;
                System.out.printf(format,
                        String.valueOf(audioBook.getISBN()),
                        audioBook.getTitle(),
                        audioBook.getAuthor(),
                        audioBook.getPublisher(),
                        String.valueOf(audioBook.getPublicationYear()),
                        audioBook.getCategory(),
                        audioBook.getBookType(),
                        "-", "-", "-", "-", "-", "-",
                        audioBook.getNarratorName(),
                        audioBook.getAudioFormat(),
                        String.valueOf(audioBook.getAudioLength()));
            }
        }
        System.out.println("\n===================================== END BOOK LIST =============================\n");
    }

    // Add a new book
    public void addBook() throws Exception {
        System.out.print("Enter the book title. (mandatory - max 30 characters): ");
        String title = scn.nextLine();
        if (title.isEmpty() || title.length() > 30) {
            throw new Exception("Invalid book title");
        }

        System.out.println("Choose the book category (mandatory): ");
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

            EBook eBook = new EBook(title, author, publisher, publicationYear, fileFormat, drmProtected, bookCategory);
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
                    audioLength, bookCategory);
            this.books.add(audioBook);
        }

        utils.saveData("./db/books.txt", this.books);
    }

    /**
     * Updates a book in the library management system.
     * 
     * @throws Exception if the book is not found or any other error occurs
     */
    public void updateBook() throws Exception {
        System.out.print("Enter book ISBN number: ");
        long isbn = Long.parseLong(scn.nextLine());
        if (isbn < 0) {
            throw new Exception("Invalid book ISBN");
        }

        // Find the book by ISBN
        int bookIndex = IntStream.range(0, this.books.size())
                .filter(i -> this.books.get(i).getISBN() == isbn)
                .findFirst().orElse(-1);
        if (bookIndex == -1) {
            throw new Exception("Book not found");
        }

        // Get the book details
        Book book = this.books.get(bookIndex);
        System.out.print(
                "Enter the book title.(current: " + book.getTitle() + ") (press enter to skip - max 30 characters): ");
        String title = scn.nextLine();
        if (!title.isEmpty() && (title.length() > 30)) {
            throw new Exception("Invalid book title");
        }
        if (!title.isEmpty()) {
            book.setTitle(title);
        }

        System.out.print("Enter the book author.(current: " + book.getAuthor()
                + ") (press enter to skip - max 30 characters): ");
        String author = scn.nextLine();
        if (!author.isEmpty() && (author.length() > 30)) {
            throw new Exception("Invalid book author");
        }
        if (!author.isEmpty()) {
            book.setAuthor(author);
        }

        System.out.print("Enter the book publisher.(current: " + book.getPublisher()
                + ") (press enter to skip - max 30 characters): ");
        String publisher = scn.nextLine();
        if (!publisher.isEmpty() && (publisher.length() > 30)) {
            throw new Exception("Invalid book publisher");
        }
        if (!publisher.isEmpty()) {
            book.setPublisher(publisher);
        }

        System.out.print("Enter the book publication year.(current: " + book.getPublicationYear()
                + ") (press enter to skip): ");
        String publicationYear = scn.nextLine();
        if (!publicationYear.isEmpty() && Integer.parseInt(publicationYear) <= 0) {
            throw new Exception("Invalid book publication year");
        }
        if (!publicationYear.isEmpty()) {
            book.setPublicationYear(Integer.parseInt(publicationYear));
        }

        if (book instanceof PhysicalBook) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            System.out.print(
                    "Enter the book total copies.(current: " + physicalBook.getTotalCopies()
                            + ") (press enter to skip - it will be added to the current total copies and available copies): ");
            String totalCopies = scn.nextLine();
            if (!totalCopies.isEmpty() && Integer.parseInt(totalCopies) < 0) {
                throw new Exception("Invalid book total copies");
            }
            if (!totalCopies.isEmpty()) {
                physicalBook.setTotalCopies(physicalBook.getTotalCopies() + Integer.parseInt(totalCopies));
                physicalBook.setAvailableCopies(physicalBook.getAvailableCopies() + Integer.parseInt(totalCopies));
            }
        }

        this.books.set(bookIndex, book);
        utils.saveData("./db/books.txt", this.books);
        System.out.println("Book updated successfully");

    }

    /**
     * Searches a book in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void searchBook() throws Exception {
        System.out.println("1.Search Book by ISBN");
        System.out.println("2.Search Book by Title");
        System.out.println("3.Search Book by Author");
        String value = scn.nextLine().split("\\s+")[0];
        switch (value) {
            case "1":
                searchBookByISBN();
                break;
            case "2":
                searchBookByTitle();
                break;
            case "3":
                searchBookByAuthor();
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    /**
     * Searches a book by ISBN in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void searchBookByISBN() throws Exception {
        System.out.print("Enter book ISBN number: ");
        long isbn = Long.parseLong(scn.nextLine());
        if (isbn < 0) {
            throw new Exception("Invalid book ISBN");
        }

        ArrayList<Book> book = new ArrayList<Book>();
        for (Book b : this.books) {
            if (b.getISBN() == isbn) {
                book.add(b);
                break;
            }
        }
        if (book.isEmpty()) {
            throw new Exception("Book not found");
        }
        showBookList(book);
    }

    /**
     * Searches a book by title in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void searchBookByTitle() throws Exception {
        System.out.print("Enter book title: ");
        String title = scn.nextLine();
        if (title.isEmpty()) {
            throw new Exception("Invalid book title");
        }

        ArrayList<Book> book = new ArrayList<Book>();
        for (Book b : this.books) {
            if (b.getTitle().toLowerCase().contains(title.toLowerCase())) {
                book.add(b);
            }
        }
        if (book.isEmpty()) {
            throw new Exception("Book not found");
        }
        showBookList(book);
    }

    /**
     * Searches a book by author in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void searchBookByAuthor() throws Exception {
        System.out.print("Enter book author: ");
        String author = scn.nextLine();
        if (author.isEmpty()) {
            throw new Exception("Invalid book author");
        }

        ArrayList<Book> book = new ArrayList<Book>();
        for (Book b : this.books) {
            if (b.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                book.add(b);
            }
        }
        if (book.isEmpty()) {
            throw new Exception("Book not found");
        }
        showBookList(book);
    }

    /**
     * Views the details of a book in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void viewBookDetails() throws Exception {
        System.out.println("Enter book ISBN number: ");
        long isbn = Long.parseLong(scn.nextLine());
        if (isbn < 0) {
            throw new Exception("Invalid book ISBN");
        }

        ArrayList<Book> book = new ArrayList<Book>();
        for (Book b : this.books) {
            if (b.getISBN() == isbn) {
                book.add(b);
                break;
            }
        }
        if (book.isEmpty()) {
            throw new Exception("Book not found");
        }
        showBookList(book);
    }

    /**
     * Checks the availability of a book in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void checkBookAvailability() throws Exception {
        System.out.print("Enter book ISBN number: ");
        long isbn = Long.parseLong(scn.nextLine());
        if (isbn < 0) {
            throw new Exception("Invalid book ISBN");
        }

        Book book = null;
        for (Book b : this.books) {
            if (b.getISBN() == isbn) {
                book = b;
                break;
            }
        }
        if (book == null) {
            throw new Exception("Book not found");
        }
        if (book instanceof PhysicalBook) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            if (physicalBook.getAvailableCopies() > 0) {
                System.out.println("Book is available");
            } else {
                System.out.println("Book is not available");
            }
        } else {
            System.out.println("Book is available");
        }
    }
}
