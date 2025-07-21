package model;

import common.BookCategory;

public class PhysicalBook extends Book {
    private static final long serialVersionUID = 1L;

    private int pages;
    private int totalCopies;
    private int availableCopies;
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
