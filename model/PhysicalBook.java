package model;

import common.BookCategory;

public class PhysicalBook extends Book {
    private int pages;
    private int totalCopies;
    private int availableCopies;
    private int reservedCopies;

    public PhysicalBook(String title, String author, String publisher, int publicationYear, int pages, int totalCopies, BookCategory category) {
        super(title, author, publisher, publicationYear, "Physical Book");
        this.pages = pages;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.reservedCopies = 0;
    }

    @Override
    public String toString() {
        return super.toString() + ", pages=" + pages + ", totalCopies=" + totalCopies + ", availableCopies=" + availableCopies + ", reservedCopies=" + reservedCopies + "]";
    }
}
