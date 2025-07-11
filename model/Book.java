package model;

import java.io.Serializable;

import common.BookCategory;
import common.utils;

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

public abstract class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The ISBN of the book */
    private long ISBN;

    /** The title of the book */
    private String title;

    /** The author of the book */
    private String author;

    /** The publisher of the book */
    private String publisher;

    /** The publication year of the book */
    private int publicationYear;

    /** The category of the book */
    private BookCategory category;

    /** The type of the book */
    private String bookType;

    /** The availability status of the book */
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
    public Book(String title, String author, String publisher, int publicationYear, String bookType,
            BookCategory category) {
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
        return "Book [ISBN=" + this.ISBN + ", title=" + this.title + ", author=" + this.author + ", publisher="
                + this.publisher + ", publicationYear=" + this.publicationYear + ", category=" + this.category
                + ", bookType=" + this.bookType;
    }

}
