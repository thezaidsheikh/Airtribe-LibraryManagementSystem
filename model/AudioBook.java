package model;

import common.BookCategory;

public class AudioBook extends Book {
    private static final long serialVersionUID = 1L;

    private String narratorName;
    private String audioFormat;
    private int audioLength;

    public AudioBook(String title, String author, String publisher, int publicationYear, BookCategory category,
            String narratorName, String audioFormat, int audioLength) {
        super(title, author, publisher, publicationYear, category, "Audio Book");
        this.narratorName = narratorName;
        this.audioFormat = audioFormat;
        this.audioLength = audioLength;
    }

    public String getNarratorName() {
        return this.narratorName;
    }

    public String getAudioFormat() {
        return this.audioFormat;
    }

    public int getAudioLength() {
        return this.audioLength;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + ", narratorName=" + narratorName + ", audioFormat=" + audioFormat + ", audioLength="
                + audioLength;
    }
}
