package main;

import java.util.Scanner;

import service.BookService;

/**
 * Subclass of InvoiceManagementSystem
 */
public class LibraryManagementMenu {
    Scanner scn = new Scanner(System.in);
    BookService bookService = new BookService();
    /**
     * Sets up the application to show options
     */
    protected void setup() throws Exception {
        this.bookService.loadBooks();
        System.out.println("========= WELCOME TO LIBRARY MANAGEMENT SYSTEM =====================");
        System.out.println("========== KINDLY SELECT THE OPTION ======================\n");
        while (true) {
            // Book related options
            System.out.println("=== Book Operations ===\n");
            System.out.println("1.Add New Book");
            System.out.println("2.Update Book Info");
            System.out.println("3.Search Books");
            System.out.println("4.View Book Details");
            System.out.println("5.Check Availability\n");

            // Member related options
            System.out.println("=== Member Operations ===\n");
            System.out.println("6.Register Member");
            System.out.println("7.Update Member Info");
            System.out.println("8.Search Members");
            System.out.println("9.View Member History");
            System.out.println("10.Fine Management\n");

            // Borrowing related options
            System.out.println("=== Borrowing Operations ===\n");
            System.out.println("11.Issue Book");
            System.out.println("12.Return Book");
            System.out.println("13.Renew Book");
            System.out.println("14.Reserve Book");
            System.out.println("15.View Overdue Books\n");

            // Advance features related options
            System.out.println("=== Advanced Features ===\n");
            System.out.println("16.Book Recommendations");
            System.out.println("17.Advanced Search");
            System.out.println("18.Popular Books Report");
            System.out.println("19.Member Analytics\n");

            // Reports related options
            System.out.println("=== Reports & Analytics ===\n");
            System.out.println("20.Borrowing Reports");
            System.out.println("21.Fine Collection Reports");
            System.out.println("22.Book Popularity Analysis");
            System.out.println("23.Member Engagement Reports\n");

            // System related options
            System.out.println("=== System Operations ===\n");
            System.out.println("24.Data Backup");
            System.out.println("25.Data Import/Export");
            System.out.println("26.System Configuration\n");

            // Exit operation
            System.out.println("27.Exit\n");

            String choice = scn.nextLine();
            System.out.println("You have selected: " + choice);

            switch (choice) {
                case "1":
                    System.out.println("====================== START - ADD NEW BOOK ======================\n");
                    bookService.addBook();
                    System.out.println("====================== END - ADD NEW BOOK ======================\n");
                    break; 
                case "27":
                    System.out.println("Thank you for using the Library Management System. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        }
    }
}
