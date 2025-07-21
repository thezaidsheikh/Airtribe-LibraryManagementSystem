package model;

import common.BookCategory;

/**
 * AudioBook class representing an audio book in the Library Management System.
 * This class extends the Book class to provide specific functionality for audio
 * books,
 * including narrator information, audio format specifications, and duration
 * tracking.
 * 
 * <p>
 * Audio books are digital media that are always available for borrowing since
 * they
 * don't have physical copy limitations. They include additional metadata
 * specific to
 * audio content such as narrator details, audio format (MP3, AAC, etc.), and
 * duration.
 * </p>
 * 
 * <p>
 * Key features include:
 * <ul>
 * <li>Narrator name tracking for voice talent identification</li>
 * <li>Audio format specification (MP3, AAC, WAV, etc.)</li>
 * <li>Duration tracking in hours for content length</li>
 * <li>Unlimited availability (no copy restrictions)</li>
 * <li>Integration with the library's digital collection</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Usage example:
 * 
 * <pre>
 * AudioBook audioBook = new AudioBook(
 *         "The Great Gatsby",
 *         "F. Scott Fitzgerald",
 *         "Penguin Audio",
 *         2020,
 *         BookCategory.Fiction,
 *         "Jim Dale",
 *         "MP3",
 *         8);
 * </pre>
 * </p>
 * 
 * @see Book
 * @see BookCategory
 * @author Library Management System Team
 * @version 1.0
 * @since 2025-07-21
 */
public class AudioBook extends Book {
    /**
     * The serialization version UID for ensuring version compatibility
     * during object serialization/deserialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the narrator or voice actor who performed the audio book.
     * This field helps users identify their preferred narrators and provides
     * important information for audio book selection.
     */
    private String narratorName;

    /**
     * The audio format of the book (e.g., MP3, AAC, WAV, FLAC).
     * This information is crucial for compatibility with different playback devices
     * and helps users determine if they can play the audio book on their preferred
     * device.
     */
    private String audioFormat;

    /**
     * The total duration of the audio book in hours.
     * This provides users with an estimate of the listening time required
     * and helps in planning their reading/listening schedule.
     */
    private int audioLength;

    /**
     * Constructs a new AudioBook with the specified details.
     * <p>
     * This constructor creates an audio book instance with all the necessary
     * information including narrator details, audio format, and duration.
     * The book type is automatically set to "Audio Book".
     * </p>
     * 
     * @param title           The title of the audio book (cannot be null or empty)
     * @param author          The author of the audio book (cannot be null or empty)
     * @param publisher       The publisher of the audio book (cannot be null or
     *                        empty)
     * @param publicationYear The year the audio book was published (must be
     *                        positive)
     * @param category        The category/genre of the audio book (cannot be null)
     * @param narratorName    The name of the narrator (cannot be null or empty)
     * @param audioFormat     The audio format (e.g., "MP3", "AAC") (cannot be null
     *                        or empty)
     * @param audioLength     The duration in hours (must be positive)
     * 
     * @throws IllegalArgumentException if any parameter is null, empty, or invalid
     * 
     * @see Book#Book(String, String, String, int, BookCategory, String)
     * @see BookCategory
     */
    public AudioBook(String title, String author, String publisher, int publicationYear, BookCategory category,
            String narratorName, String audioFormat, int audioLength) {
        super(title, author, publisher, publicationYear, category, "Audio Book");
        this.narratorName = narratorName;
        this.audioFormat = audioFormat;
        this.audioLength = audioLength;
    }

    /**
     * Retrieves the name of the narrator who performed this audio book.
     * <p>
     * The narrator is a crucial aspect of audio books as their performance
     * significantly impacts the listening experience. This information helps
     * users identify books by their favorite narrators and make informed
     * selection decisions.
     * </p>
     * 
     * @return The narrator's name as a String (never null)
     * 
     * @see #AudioBook(String, String, String, int, BookCategory, String, String,
     *      int)
     */
    public String getNarratorName() {
        return this.narratorName;
    }

    /**
     * Retrieves the audio format of this audio book.
     * <p>
     * The audio format determines compatibility with various playback devices
     * and applications. Common formats include MP3 (widely compatible), AAC
     * (high quality, Apple devices), WAV (uncompressed), and FLAC (lossless
     * compression).
     * This information is essential for users to ensure they can play the audio
     * book
     * on their preferred devices.
     * </p>
     * 
     * @return The audio format as a String (e.g., "MP3", "AAC", "WAV") (never null)
     * 
     * @see #AudioBook(String, String, String, int, BookCategory, String, String,
     *      int)
     */
    public String getAudioFormat() {
        return this.audioFormat;
    }

    /**
     * Retrieves the total duration of this audio book in hours.
     * <p>
     * The audio length provides users with an estimate of the time commitment
     * required to complete the book. This information is valuable for planning
     * listening schedules, especially for longer works or when users have
     * limited time available for audio book consumption.
     * </p>
     * 
     * @return The duration in hours as an integer (always positive)
     * 
     * @see #AudioBook(String, String, String, int, BookCategory, String, String,
     *      int)
     */
    public int getAudioLength() {
        return this.audioLength;
    }

    /**
     * Determines if this audio book is available for borrowing.
     * <p>
     * Audio books are digital media and therefore always available for borrowing.
     * Unlike physical books, there are no copy limitations or availability
     * constraints
     * for digital audio books. Multiple users can simultaneously borrow and listen
     * to the same audio book without affecting availability for others.
     * </p>
     * 
     * <p>
     * This implementation always returns true, indicating unlimited availability.
     * The library system can issue this audio book to any eligible member at any
     * time
     * without checking for physical copy availability.
     * </p>
     * 
     * @return Always returns true, indicating the audio book is always available
     * 
     * @see Book#isAvailable()
     * @see PhysicalBook#isAvailable()
     * @see EBook#isAvailable()
     */
    @Override
    public boolean isAvailable() {
        return true;
    }

    /**
     * Returns a string representation of this audio book.
     * <p>
     * The string includes all relevant audio book information in a comma-separated
     * format, including the base book information (ISBN, title, author, publisher,
     * publication year, category, book type) plus audio-specific details (narrator,
     * format, and length).
     * </p>
     * 
     * <p>
     * Example output:
     * 
     * <pre>
     * ISBN=1234567890123, title=The Great Gatsby, author=F. Scott Fitzgerald, 
     * publisher=Penguin Audio, publicationYear=2020, category=Fiction, 
     * bookType=Audio Book, narratorName=Jim Dale, audioFormat=MP3, audioLength=8
     * </pre>
     * </p>
     * 
     * @return A string containing all audio book details for display or logging
     *         purposes
     * 
     * @see Book#toString()
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return super.toString() + ", narratorName=" + narratorName + ", audioFormat=" + audioFormat + ", audioLength="
                + audioLength;
    }
}
