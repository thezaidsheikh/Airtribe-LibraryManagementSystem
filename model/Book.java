package model;

import java.io.Serializable;

import common.BookCategory;
import common.utils;

/**
 * Constructs a Book instance with the given title, author, publisher,
 * publication year, pages, total copies, category, available copies, and
 * reserved copies.
 *
 * @param title          the title of the book
 * @param author         the author of the book
 * @param publisher      the publisher of the book
 * @param publicationYear the publication year of the book
 * @param pages          the number of pages in the book
 * @param totalCopies    the total number of copies of the book
 * @param category       the category of the book
 */

public class Book implements Serializable{
    private long ISBN;
    private String title;
    private String author;
    private String publisher;
    private int publicationYear;
    private BookCategory category;
    private String bookType;

    public Book() {}

    public Book(String title, String author, String publisher, int publicationYear, String bookType) {
        this.ISBN = utils.generateId(13);
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.bookType = bookType;
    }

    @Override
    public String toString() {
        return "Book [ISBN=" + this.ISBN + ", title=" + this.title + ", author=" + this.author + ", publisher=" + this.publisher + ", publicationYear=" + this.publicationYear + ", category=" + this.category + ", bookType=" + this.bookType;
    }

}
