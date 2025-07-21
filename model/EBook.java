package model;

import common.BookCategory;

public class EBook extends Book {
    private static final long serialVersionUID = 1L;

    private String fileFormat;
    private boolean isDrmProtected;

    public EBook(String title, String author, String publisher, int publicationYear, BookCategory category,
            String fileFormat, boolean isDrmProtected) {
        super(title, author, publisher, publicationYear, category, "EBook");
        this.fileFormat = fileFormat;
        this.isDrmProtected = isDrmProtected;
    }

    public String getFileFormat() {
        return this.fileFormat;
    }

    public boolean isDrmProtected() {
        return this.isDrmProtected;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + ", fileFormat=" + fileFormat + ", isDrmProtected=" + isDrmProtected;
    }
}
