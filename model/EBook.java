package model;

public class EBook extends Book {
    private String fileFormat;
    private boolean isDrmProtected;

    public EBook(String title, String author, String publisher, int publicationYear, String fileFormat, boolean isDrmProtected) {
        super(title, author, publisher, publicationYear, "EBook");
        this.fileFormat = fileFormat;
        this.isDrmProtected = isDrmProtected;
    }

    @Override
    public String toString() {
        return super.toString() + ", fileFormat=" + fileFormat + ", isDrmProtected=" + isDrmProtected + "]";
    }
}
