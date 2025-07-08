package model;

public class AudioBook extends Book {
    private String narratorName;
    private String audioFormat;
    private int audioLength;

    public AudioBook(String title, String author, String publisher, int publicationYear, String narratorName, String audioFormat, int audioLength) {
        super(title, author, publisher, publicationYear, "Audio Book");
        this.narratorName = narratorName;
        this.audioFormat = audioFormat;
        this.audioLength = audioLength;
    }

    @Override
    public String toString() {
        return super.toString() + ", narratorName=" + narratorName + ", audioFormat=" + audioFormat + ", audioLength=" + audioLength + "]";
    }
}
