package common;

// Book category for library management
public enum BookCategory {
    Fiction, Non_Fiction, Science, Technology, History, Biography, Self_Help, Children, Poetry, Drama;

    public static BookCategory getCategoryByNumber(int number) throws Exception {
        for (BookCategory category : BookCategory.values()) {
            if (category.ordinal() == number - 1) {
                return category;
            }
        }
        throw new Exception("Invalid book category");
    }

    public static BookCategory getCategoryByName(String name) throws Exception {
        for (BookCategory category : BookCategory.values()) {
            if (category.name().equalsIgnoreCase(name)) {
                return category;
            }
        }
        throw new Exception("Invalid book category");
    }
}
