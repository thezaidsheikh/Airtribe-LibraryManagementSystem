package main;

/**
 * The main entry point for the Library Management System application.
 * This class initializes the system and starts the main menu interface.
 * 
 * <p>The Library Management System provides a comprehensive solution for managing
 * library operations including book management, member management, book issuing,
 * returns, reservations, and various reporting features.</p>
 * 
 * @author Library Management System Team
 * @version 1.0
 * @since 2025-07-21
 */
public class LibraryManagementSystem {
    
    /**
     * The main method serves as the entry point for the Library Management System.
     * It creates an instance of LibraryManagementMenu and starts the application.
     * 
     * @param args command-line arguments (not currently used)
     * @throws Exception if any error occurs during application startup
     */
    public static void main(String[] args) throws Exception {
        LibraryManagementMenu menu = new LibraryManagementMenu();
        menu.setup();
    }
}
