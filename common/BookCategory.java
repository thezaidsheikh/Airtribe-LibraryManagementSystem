package common;

/**
 * Represents the various categories that a book can belong to in the library
 * system.
 * This enum defines the standard book categories and provides utility methods
 * to retrieve category information by different identifiers.
 */
public enum BookCategory {
    /** Represents fictional literature including novels and short stories */
    Fiction,
    /** Represents non-fictional works based on facts and real events */
    Non_Fiction,
    /** Books related to scientific subjects and research */
    Science,
    /** Books about technological advancements and computing */
    Technology,
    /** Works covering historical events and periods */
    History,
    /** Books about people's lives written by someone else */
    Biography,
    /** Books focused on personal development and improvement */
    Self_Help,
    /** Literature specifically written for young readers */
    Children,
    /** Literary works in verse form */
    Poetry,
    /** Works written for theatrical performance */
    Drama;

    /**
     * Retrieves a BookCategory based on its numerical representation.
     * The number should correspond to the category's position in the enum (1-based
     * index).
     *
     * @param number the position number of the category (1-based index)
     * @return the corresponding BookCategory
     * @throws Exception if the provided number doesn't match any category
     */
    public static BookCategory getCategoryByNumber(int number) throws Exception {
        for (BookCategory category : BookCategory.values()) {
            if (category.ordinal() == number - 1) {
                return category;
            }
        }
        throw new Exception("Invalid book category number: " + number);
    }

    /**
     * Retrieves a BookCategory by its name (case-insensitive).
     * The name should match the enum constant name (e.g., "Fiction", "Science").
     *
     * @param name the name of the category (case-insensitive)
     * @return the corresponding BookCategory
     * @throws Exception if no category with the given name exists
     */
    public static BookCategory getCategoryByName(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Category name cannot be null or empty");
        }

        for (BookCategory category : BookCategory.values()) {
            if (category.name().equalsIgnoreCase(name)) {
                return category;
            }
        }
        throw new Exception("Invalid book category name: " + name);
    }
}
