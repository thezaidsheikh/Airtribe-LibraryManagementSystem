package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import common.BookCategory;
import common.utils;
import model.AudioBook;
import model.Book;
import model.EBook;
import model.PhysicalBook;

/**
 * Service class for managing books in the library management system.
 * <p>
 * This class provides functionality to add, update, search, and manage books
 * of different types (Physical, E-Book, Audio Book). It handles the business
 * logic for book-related operations and maintains the book collection.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2023-01-01
 * @see Book
 * @see PhysicalBook
 * @see EBook
 * @see AudioBook
 */
public class BookService {
    /** Scanner instance for reading user input */
    private Scanner scn = new Scanner(System.in);
    
    /** List to store all books in the library */
    private List<Book> books = new ArrayList<>();

    /**
     * Loads book data from persistent storage.
     * <p>
     * This method reads book data from the serialized file located at "./db/books.ser"
     * and populates the internal books list. If the file doesn't exist or is empty,
     * the books list will be initialized as an empty list.
     * 
     * @see utils#loadData(String)
     */
    public void loadBooks() {
        this.books = utils.loadData("./db/books.ser");
    }

    /**
     * Helper method to display a list of books.
     */
    /**
     * Displays a formatted list of books in a tabular format.
     * <p>
     * The table includes different columns based on the book type (Physical, E-Book, Audio Book).
     * For each book, relevant details are displayed in a properly formatted manner.
     *
     * @param booksList The list of books to display (can be empty)
     * 
     * @see Book
     * @see PhysicalBook
     * @see EBook
     * @see AudioBook
     */
    protected void showBookList(List<Book> booksList) {
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

    /**
     * Adds a new book to the library collection.
     * <p>
     * This interactive method prompts the user to enter book details and creates
     * the appropriate book type (Physical, E-Book, or Audio Book) based on user input.
     * The book is then added to the internal collection and persisted to storage.
     *
     * @throws Exception If any validation fails or an error occurs during the operation
     * @throws NumberFormatException If numeric input is not in the correct format
     * 
     * @see PhysicalBook
     * @see EBook
     * @see AudioBook
     * @see #updateBookInDatabase()
     */
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
        if (category < 1 || category > 10 || BookCategory.getCategoryByNumber(category) == null) {
            throw new Exception("Invalid book category");
        }
        BookCategory bookCategory = BookCategory.getCategoryByNumber(category);
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

            PhysicalBook physicalBook = new PhysicalBook(title, author, publisher, publicationYear, bookCategory,
                    pages, totalCopies);
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

            EBook eBook = new EBook(title, author, publisher, publicationYear, bookCategory, fileFormat, drmProtected);
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

            AudioBook audioBook = new AudioBook(title, author, publisher, publicationYear, bookCategory, narratorName,
                    audioFormat, audioLength);
            this.books.add(audioBook);
        }

        this.updateBookInDatabase();
    }

    /**
     * Updates an existing book's information in the library collection.
     * <p>
     * This method allows updating various attributes of a book identified by its ISBN.
     * Only non-empty fields provided by the user will be updated. The changes are
     * then persisted to storage.
     *
     * @throws Exception If the book is not found, validation fails, or any other error occurs
     * @throws NumberFormatException If numeric input is not in the correct format
     * 
     * @see #getBookById(long)
     * @see #updateBookInDatabase()
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
        this.updateBookInDatabase();
        System.out.println("Book updated successfully");

    }

    /**
     * Provides an interactive menu for searching books by different criteria.
     * <p>
     * This method displays a menu with options to search books by:
     * 1. ISBN
     * 2. Title
     * 3. Author
     * 4. Multiple authors
     * 5. Category and publication year
     * 
     * Based on user selection, it calls the appropriate search method.
     *
     * @throws Exception If an I/O error occurs during user input
     * 
     * @see #searchBookByISBN()
     * @see #searchBookByTitle()
     * @see #searchBookByAuthor()
     * @see #searchBookByMultipleAuthor()
     * @see #filterAvailableBooksByCategoryAndPublicationYear()
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
     * Searches for a book by its ISBN number.
     * <p>
     * This method prompts the user to enter an ISBN and displays the matching book
     * if found. The search is case-insensitive and performs an exact match on the ISBN.
     *
     * @throws Exception If the input format is invalid or no book is found
     * @throws NumberFormatException If the input cannot be parsed as a long
     * 
     * @see #getBookById(long)
     * @see #showBookList(List)
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
     * Searches for books by title.
     * <p>
     * This method performs a case-insensitive partial match search on book titles.
     * It displays all books whose titles contain the search term.
     *
     * @throws Exception If the input is empty or no matching books are found
     * 
     * @see #showBookList(List)
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
     * Searches for books by author.
     * <p>
     * This method performs a case-insensitive partial match search on book authors.
     * It displays all books whose authors' names contain the search term.
     *
     * @throws Exception If the input is empty or no matching books are found
     * 
     * @see #getBooksByAuthor(String)
     * @see #showBookList(List)
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
     * Displays detailed information about a specific book.
     * <p>
     * This method prompts the user for a book ISBN and displays all available
     * details for that book, including type-specific information.
     *
     * @throws Exception If the book is not found or an error occurs
     * @throws NumberFormatException If the input cannot be parsed as a long
     * 
     * @see #getBookById(long)
     * @see #showBookList(List)
     */
    public void viewBookDetails() throws Exception {
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
     * Checks and displays the availability status of a book.
     * <p>
     * This method checks if a book is available for borrowing based on its type:
     * - For physical books: Checks available copies > 0
     * - For e-books/audio books: Always available unless marked otherwise
     *
     * @throws Exception If the book is not found or an error occurs
     * @throws NumberFormatException If the input cannot be parsed as a long
     * 
     * @see #getBookById(long)
     * @see PhysicalBook#getAvailableCopies()
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
            if (physicalBook.isAvailable()) {
                System.out.println("Book is available");
            } else {
                System.out.println("Book is not available");
            }
        } else {
            System.out.println("Book is available");
        }
    }

    /**
     * Retrieves a book from the collection by its ISBN.
     *
     * @param isbn The ISBN of the book to retrieve
     * @return The book with the matching ISBN, or null if not found
     * 
     * @see Book#getISBN()
     */
    protected Book getBookById(long isbn) {
        for (Book b : this.books) {
            if (b.getISBN() == isbn) {
                return b;
            }
        }
        return null;
    }

    /**
     * Updates a book in the internal books list.
     * <p>
     * If a book with the same ISBN exists in the list, it will be updated.
     * If not, the book will be added to the list.
     *
     * @param book The book to update or add
     * @throws Exception If the book is not found or an error occurs during update
     * 
     * @see #updateBookInDatabase()
     */
    protected void updateBookInList(Book book) throws Exception {
        int bookIndex = IntStream.range(0, this.books.size())
                .filter(i -> this.books.get(i).getISBN() == book.getISBN())
                .findFirst()
                .orElse(-1);
        if (bookIndex == -1) {
            throw new Exception("Book not found");
        }
        this.books.set(bookIndex, book);
    }

    /**
     * Saves the current book collection to persistent storage.
     * <p>
     * This method serializes the current list of books and saves it to
     * "./db/books.ser". It should be called after any modifications to
     * the book collection to ensure data persistence.
     *
     * @throws Exception If an error occurs during file I/O operations
     * @see utils#saveData(String, Object)
     */
    protected void updateBookInDatabase() throws Exception {
        try {
            utils.saveData("./db/books.ser", this.books);
        } catch (Exception e) {
            throw new Exception("Failed to save book data: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all books by a specific author.
     *
     * @param author The name of the author to search for (case-insensitive)
     * @return A list of books written by the specified author, or an empty list if none found
     * 
     * @see Book#getAuthor()
     * @see String#toLowerCase()
     * @see String#contains(CharSequence)
     */
    protected List<Book> getBooksByAuthor(String author) {
        return this.books.stream().filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    /**
     * Replaces the current book collection with a new list of books.
     * <p>
     * This method is primarily used for loading books from persistent storage
     * or restoring from a backup.
     *
     * @param books The new list of books to use
     * 
     * @see #loadBooks()
     */
    protected void replaceBookList(List<Book> books) {
        this.books = books;
    }

    /**
     * Searches for books by multiple authors.
     * <p>
     * This method allows searching for books written by any of the specified authors.
     * The search is case-insensitive and performs partial matches on author names.
     *
     * @throws Exception If no authors are provided or no matching books are found
     * 
     * @see #getBooksByAuthor(String)
     * @see #showBookList(List)
     */
    public void searchBookByMultipleAuthor() throws Exception {
        System.out.print("Enter book authors: ");
        String authorNames = scn.nextLine();
        if (authorNames.isEmpty()) {
            throw new Exception("Invalid book author");
        }
        List<String> authors = Arrays.asList(authorNames.split(","));
        List<Book> result = this.books.stream()
                .filter(book -> authors.stream().anyMatch(author -> book.getAuthor().contains(author)))
                .collect(Collectors.toList());
        showBookList(result);
    }

    /**
     * Filters and displays available books by category and publication year range.
     * <p>
     * This method prompts the user to select a book category and enter a range of
     * publication years, then displays all available books that match the criteria.
     *
     * @throws Exception If invalid input is provided or no matching books are found
     * 
     * @see BookCategory
     * @see #showBookList(List)
     */
    protected void filterAvailableBooksByCategoryAndPublicationYear() throws Exception {
        System.out.print(
                "Enter book category - (Fiction, Non_Fiction, Science, Technology, History, Biography, Self_Help, Children, Poetry, Drama): ");
        String category = scn.nextLine();
        if (category.isEmpty() && BookCategory.getCategoryByName(category) == null) {
            throw new Exception("Invalid book category");
        }
        System.out.print("Enter book publication year: ");
        String publicationYear = scn.nextLine();
        if (publicationYear.isEmpty()) {
            throw new Exception("Invalid book publication year");
        }
        List<Book> result = this.books.stream()
                .filter(book -> book.getCategory().name().equalsIgnoreCase(category))
                .filter(book -> book.getPublicationYear() == Integer.parseInt(publicationYear))
                .collect(Collectors.toList());
        showBookList(result);
    }
}
