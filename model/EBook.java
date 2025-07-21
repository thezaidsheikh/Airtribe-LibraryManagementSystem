package model;

import common.BookCategory;

/**
 * EBook class representing an electronic book in the Library Management System.
 * This class extends the Book class to provide specific functionality for
 * digital books,
 * including file format specifications and Digital Rights Management (DRM)
 * protection status.
 * 
 * <p>
 * Electronic books are digital publications that are always available for
 * borrowing
 * since they don't have physical copy limitations. They include additional
 * metadata
 * specific to digital content such as file format compatibility and DRM
 * protection status.
 * </p>
 * 
 * <p>
 * Key features include:
 * <ul>
 * <li>File format specification (PDF, EPUB, MOBI, etc.)</li>
 * <li>DRM protection status tracking for licensing compliance</li>
 * <li>Unlimited availability (no copy restrictions)</li>
 * <li>Integration with digital reading platforms</li>
 * <li>Cross-device compatibility information</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Usage example:
 * 
 * <pre>
 * EBook ebook = new EBook(
 *         "Digital Fortress",
 *         "Dan Brown",
 *         "St. Martin's Press",
 *         2019,
 *         BookCategory.Fiction,
 *         "EPUB",
 *         true);
 * </pre>
 * </p>
 * 
 * @see Book
 * @see BookCategory
 * @author Library Management System Team
 * @version 1.0
 * @since 2025-07-21
 */
public class EBook extends Book {
    /**
     * The serialization version UID for ensuring version compatibility
     * during object serialization/deserialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The file format of the electronic book (e.g., PDF, EPUB, MOBI, AZW).
     * This information is crucial for determining compatibility with different
     * e-reader devices and applications, helping users select books they can
     * actually read on their preferred devices.
     */
    private String fileFormat;

    /**
     * Indicates whether the electronic book is protected by Digital Rights
     * Management (DRM).
     * DRM protection affects how the book can be shared, copied, or transferred
     * between devices.
     * This information is important for users to understand usage limitations and
     * compatibility
     * with their reading devices and applications.
     */
    private boolean isDrmProtected;

    /**
     * Constructs a new EBook with the specified details.
     * <p>
     * This constructor creates an electronic book instance with all the necessary
     * information including file format and DRM protection status.
     * The book type is automatically set to "EBook".
     * </p>
     * 
     * @param title           The title of the electronic book (cannot be null or
     *                        empty)
     * @param author          The author of the electronic book (cannot be null or
     *                        empty)
     * @param publisher       The publisher of the electronic book (cannot be null
     *                        or empty)
     * @param publicationYear The year the electronic book was published (must be
     *                        positive)
     * @param category        The category/genre of the electronic book (cannot be
     *                        null)
     * @param fileFormat      The file format (e.g., "PDF", "EPUB", "MOBI") (cannot
     *                        be null or empty)
     * @param isDrmProtected  Whether the book is protected by DRM (true if
     *                        protected, false otherwise)
     * 
     * @throws IllegalArgumentException if any parameter is null, empty, or invalid
     * 
     * @see Book#Book(String, String, String, int, BookCategory, String)
     * @see BookCategory
     */
    public EBook(String title, String author, String publisher, int publicationYear, BookCategory category,
            String fileFormat, boolean isDrmProtected) {
        super(title, author, publisher, publicationYear, category, "EBook");
        this.fileFormat = fileFormat;
        this.isDrmProtected = isDrmProtected;
    }

    /**
     * Retrieves the file format of this electronic book.
     * <p>
     * The file format determines compatibility with various e-reader devices and
     * applications.
     * Common formats include:
     * <ul>
     * <li>EPUB - Open standard, widely supported</li>
     * <li>PDF - Portable Document Format, universal compatibility</li>
     * <li>MOBI - Amazon Kindle format</li>
     * <li>AZW - Amazon's proprietary format</li>
     * </ul>
     * This information helps users determine if they can read the book on their
     * preferred device.
     * </p>
     * 
     * @return The file format as a String (e.g., "EPUB", "PDF", "MOBI") (never
     *         null)
     * 
     * @see #EBook(String, String, String, int, BookCategory, String, boolean)
     */
    public String getFileFormat() {
        return this.fileFormat;
    }

    /**
     * Determines whether this electronic book is protected by Digital Rights
     * Management (DRM).
     * <p>
     * DRM protection affects how users can interact with the book:
     * <ul>
     * <li>DRM-protected books may have restrictions on copying, printing, or
     * sharing</li>
     * <li>They may be limited to specific devices or applications</li>
     * <li>Transfer between devices might require authorization</li>
     * <li>Some features like text-to-speech might be disabled</li>
     * </ul>
     * This information is crucial for users to understand usage limitations before
     * borrowing.
     * </p>
     * 
     * @return true if the book is DRM-protected, false if it's DRM-free
     * 
     * @see #EBook(String, String, String, int, BookCategory, String, boolean)
     */
    public boolean isDrmProtected() {
        return this.isDrmProtected;
    }

    /**
     * Determines if this electronic book is available for borrowing.
     * <p>
     * Electronic books are digital media and therefore always available for
     * borrowing.
     * Unlike physical books, there are no copy limitations or availability
     * constraints
     * for digital books. Multiple users can simultaneously borrow and read the same
     * electronic book without affecting availability for others.
     * </p>
     * 
     * <p>
     * This implementation always returns true, indicating unlimited availability.
     * The library system can issue this electronic book to any eligible member at
     * any time
     * without checking for physical copy availability.
     * </p>
     * 
     * @return Always returns true, indicating the electronic book is always
     *         available
     * 
     * @see Book#isAvailable()
     * @see PhysicalBook#isAvailable()
     * @see AudioBook#isAvailable()
     */
    @Override
    public boolean isAvailable() {
        return true;
    }

    /**
     * Returns a string representation of this electronic book.
     * <p>
     * The string includes all relevant electronic book information in a
     * comma-separated
     * format, including the base book information (ISBN, title, author, publisher,
     * publication year, category, book type) plus digital-specific details (file
     * format
     * and DRM protection status).
     * </p>
     * 
     * <p>
     * Example output:
     * 
     * <pre>
     * ISBN=1234567890123, title=Digital Fortress, author=Dan Brown, 
     * publisher=St. Martin's Press, publicationYear=2019, category=Fiction, 
     * bookType=EBook, fileFormat=EPUB, isDrmProtected=true
     * </pre>
     * </p>
     * 
     * @return A string containing all electronic book details for display or
     *         logging purposes
     * 
     * @see Book#toString()
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return super.toString() + ", fileFormat=" + fileFormat + ", isDrmProtected=" + isDrmProtected;
    }
}
