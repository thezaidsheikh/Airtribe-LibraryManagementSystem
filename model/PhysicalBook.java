package model;

import common.BookCategory;

/**
 * PhysicalBook class representing a physical book in the Library Management
 * System.
 * This class extends the Book class to provide specific functionality for
 * physical books,
 * including copy management, page count tracking, and availability calculations
 * based on
 * physical inventory constraints.
 * 
 * <p>
 * Physical books have limited copies and require careful inventory management.
 * Unlike digital books, physical books have availability constraints based on
 * the number
 * of copies owned by the library and how many are currently borrowed or
 * reserved.
 * </p>
 * 
 * <p>
 * Key features include:
 * <ul>
 * <li>Page count tracking for book length information</li>
 * <li>Total copies inventory management</li>
 * <li>Available copies tracking for borrowing decisions</li>
 * <li>Reserved copies management for reservation system</li>
 * <li>Dynamic availability calculation based on copy status</li>
 * <li>Support for multiple constructors for different use cases</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Copy Management Logic:
 * 
 * <pre>
 * Total Copies = Available Copies + Borrowed Copies + Reserved Copies
 * Available for Borrowing = Available Copies > 0
 * </pre>
 * </p>
 * 
 * <p>
 * Usage example:
 * 
 * <pre>
 * PhysicalBook book = new PhysicalBook(
 *         "To Kill a Mockingbird",
 *         "Harper Lee",
 *         "J.B. Lippincott & Co.",
 *         1960,
 *         BookCategory.Fiction,
 *         281,
 *         5);
 * </pre>
 * </p>
 * 
 * @see Book
 * @see BookCategory
 * @author Library Management System Team
 * @version 1.0
 * @since 2025-07-21
 */
public class PhysicalBook extends Book {
    /**
     * The serialization version UID for ensuring version compatibility
     * during object serialization/deserialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The total number of pages in the physical book.
     * This information helps users estimate reading time and understand
     * the book's length before borrowing.
     */
    private int pages;

    /**
     * The total number of copies of this book owned by the library.
     * This represents the complete inventory and is used for calculating
     * availability and managing the library's collection.
     */
    private int totalCopies;

    /**
     * The number of copies currently available for borrowing.
     * This number decreases when books are borrowed or reserved and
     * increases when books are returned or reservations are cancelled.
     */
    private int availableCopies;

    /**
     * The number of copies currently reserved by members.
     * Reserved copies are not available for immediate borrowing but are
     * held for specific members who have placed reservations.
     */
    private int reservedCopies;

    public PhysicalBook(String title, String author, String publisher, int publicationYear, BookCategory category,
            int pages, int totalCopies) {
        super(title, author, publisher, publicationYear, category, "Physical Book");
        this.pages = pages;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.reservedCopies = 0;
    }

    public PhysicalBook(String title, String author, String publisher, int publicationYear, BookCategory category,
            int pages, int totalCopies, int availableCopies, int reservedCopies) {
        super(title, author, publisher, publicationYear, category, "Physical Book");
        this.pages = pages;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.reservedCopies = reservedCopies;
    }

    public int getPages() {
        return this.pages;
    }

    public int getTotalCopies() {
        return this.totalCopies;
    }

    public int getAvailableCopies() {
        return this.availableCopies;
    }

    public int getReservedCopies() {
        return this.reservedCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void setReservedCopies(int reservedCopies) {
        this.reservedCopies = reservedCopies;
    }

    @Override
    public boolean isAvailable() {
        return this.availableCopies > 0;
    }

    @Override
    public String toString() {
        return super.toString() + ", pages=" + pages + ", totalCopies=" + totalCopies + ", availableCopies="
                + availableCopies + ", reservedCopies=" + reservedCopies;
    }
}
