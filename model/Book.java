package model;

import java.io.Serializable;

import common.BookCategory;
import common.utils;

/**
 * Abstract base class representing a book in the Library Management System.
 * This class serves as the parent class for different types of books (e.g., PhysicalBook, EBook, AudioBook).
 * It defines common attributes and behaviors that all book types share.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Unique ISBN identification</li>
 *   <li>Basic book metadata (title, author, publisher, etc.)</li>
 *   <li>Book categorization</li>
 *   <li>Availability tracking</li>
 * </ul>
 * </p>
 * 
 * @see PhysicalBook
 * @see EBook
 * @see AudioBook
 * @author Library Management System Team
 * @version 1.0
 * @since 2025-07-21
 */
public abstract class Book implements Serializable {
    /**
     * The serialization version UID for ensuring version compatibility
     * during object serialization/deserialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The International Standard Book Number (ISBN) that uniquely identifies this book.
     * Automatically generated as a 13-digit number.
     */
    private long ISBN;

    /**
     * The title of the book.
     */
    private String title;

    /**
     * The name of the book's author or authors.
     */
    private String author;

    /**
     * The name of the publishing company or organization.
     */
    private String publisher;

    /**
     * The year when the book was published.
     */
    private int publicationYear;

    /**
     * The category/genre of the book (e.g., FICTION, SCIENCE, HISTORY).
     * 
     * @see BookCategory
     */
    private BookCategory category;

    /**
     * The specific type of the book (e.g., "PHYSICAL", "EBOOK", "AUDIO").
     * Used to determine the book's behavior and properties.
     */
    private String bookType;

    /**
     * Checks if the book is currently available for borrowing.
     * Implementation is provided by subclasses as availability logic may vary
     * by book type (e.g., physical copies vs. digital copies).
     * 
     * @return true if the book is available for borrowing, false otherwise
     */
    protected abstract boolean isAvailable();

    /**
     * Constructs a Book instance with the given title, author, publisher,
     * publication year, category, and book type.
     *
     * @param title           the title of the book
     * @param author          the author of the book
     * @param publisher       the publisher of the book
     * @param publicationYear the publication year of the book
     * @param category        the category of the book
     */
    public Book(String title, String author, String publisher, int publicationYear, BookCategory category,
            String bookType) {
        this.ISBN = utils.generateId(13);
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.bookType = bookType;
        this.category = category;
    }

    /**
     * Gets the ISBN of the book
     *
     * @return the ISBN of the book
     */
    public long getISBN() {
        return this.ISBN;
    }

    /**
     * Gets the title of the book
     *
     * @return the title of the book
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the author of the book
     *
     * @return the author of the book
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Gets the publisher of the book
     *
     * @return the publisher of the book
     */
    public String getPublisher() {
        return this.publisher;
    }

    /**
     * Gets the publication year of the book
     *
     * @return the publication year of the book
     */
    public int getPublicationYear() {
        return this.publicationYear;
    }

    /**
     * Gets the category of the book
     *
     * @return the category of the book
     */
    public BookCategory getCategory() {
        return this.category;
    }

    /**
     * Gets the type of the book
     *
     * @return the type of the book
     */
    public String getBookType() {
        return this.bookType;
    }

    /**
     * Sets the title of the book
     *
     * @param title the title of the book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the author of the book
     *
     * @param author the author of the book
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets the publisher of the book
     *
     * @param publisher the publisher of the book
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Sets the publication year of the book
     *
     * @param publicationYear the publication year of the book
     */
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    @Override
    public String toString() {
        return "ISBN=" + this.ISBN + ", title=" + this.title + ", author=" + this.author + ", publisher="
                + this.publisher + ", publicationYear=" + this.publicationYear + ", category=" + this.category
                + ", bookType=" + this.bookType;
    }

}
