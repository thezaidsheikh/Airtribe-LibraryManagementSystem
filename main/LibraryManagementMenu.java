package main;

import java.util.Scanner;

import service.BookIssueService;
import service.BookService;
import service.ImportExportService;
import service.MemberService;
import service.ReservationService;

/**
 * The main menu and user interface for the Library Management System.
 * This class provides a text-based interface for users to interact with
 * various library management features including book operations, member
 * management,
 * borrowing, reservations, and system administration.
 * 
 * <p>
 * The menu is organized into several categories:
 * <ul>
 * <li>Book Operations (1-5)</li>
 * <li>Member Operations (6-10)</li>
 * <li>Borrowing Operations (11-15)</li>
 * <li>Advanced Features (16-19)</li>
 * <li>Reports & Analytics (20-23)</li>
 * <li>System Operations (24-26)</li>
 * </ul>
 * </p>
 * 
 * @author Library Management System Team
 * @version 1.0
 * @see LibraryManagementSystem
 */
public class LibraryManagementMenu {
    /** Scanner object for reading user input from the console */
    Scanner scn = new Scanner(System.in);

    /** Service for handling book-related operations */
    BookService bookService = new BookService();

    /** Service for handling member-related operations */
    MemberService memberService = new MemberService();

    /** Service for managing book reservations */
    ReservationService reservationService = new ReservationService(memberService, bookService);

    /** Service for handling book issuing and returns */
    BookIssueService bookIssueService = new BookIssueService(memberService, bookService, reservationService);

    /** Service for importing and exporting system data */
    ImportExportService importExportService = new ImportExportService(this.memberService, this.bookService,
            this.bookIssueService);

    /**
     * Initializes the Library Management System and displays the main menu.
     * This method loads all necessary data and enters the main menu loop,
     * processing user input until the application is terminated.
     * 
     * <p>
     * The setup process includes:
     * <ol>
     * <li>Loading book data from storage</li>
     * <li>Loading member data from storage</li>
     * <li>Loading book issue records</li>
     * <li>Loading reservation data</li>
     * <li>Displaying the main menu interface</li>
     * </ol>
     * </p>
     * 
     * @throws Exception if there is an error loading initial data or processing
     *                   user input
     */
    protected void setup() throws Exception {
        this.bookService.loadBooks();
        this.memberService.loadMembers();
        this.bookIssueService.loadBookIssued();
        this.reservationService.loadReservationData();

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
                case "2":
                    System.out.println("====================== START - UPDATE BOOK ======================\n");
                    bookService.updateBook();
                    System.out.println("====================== END - UPDATE BOOK ======================\n");
                    break;
                case "3":
                    System.out.println("====================== START - SEARCH BOOK ======================\n");
                    bookService.searchBook();
                    System.out.println("====================== END - SEARCH BOOK ======================\n");
                    break;
                case "4":
                    System.out.println("====================== START - VIEW BOOK DETAILS ======================\n");
                    bookService.viewBookDetails();
                    System.out.println("====================== END - VIEW BOOK DETAILS ======================\n");
                    break;
                case "5":
                    System.out
                            .println("====================== START - CHECK BOOK AVAILABILITY ======================\n");
                    bookService.checkBookAvailability();
                    System.out.println("====================== END - CHECK BOOK AVAILABILITY ======================\n");
                    break;
                case "6":
                    System.out.println("====================== START - REGISTER MEMBER ======================\n");
                    memberService.registerMember();
                    System.out.println("====================== END - REGISTER MEMBER ======================\n");
                    break;
                case "7":
                    System.out.println("====================== START - UPDATE MEMBER INFO ======================\n");
                    memberService.updateMemberInfo();
                    System.out.println("====================== END - UPDATE MEMBER INFO ======================\n");
                    break;
                case "8":
                    System.out.println("====================== START - SEARCH MEMBER ======================\n");
                    memberService.searchMember();
                    System.out.println("====================== END - SEARCH MEMBER ======================\n");
                    break;
                case "9":
                    System.out.println("====================== START - VIEW MEMBER HISTORY ======================\n");
                    memberService.viewMemberHistory();
                    System.out.println("====================== END - VIEW MEMBER HISTORY ======================\n");
                    break;
                case "10":
                    System.out.println("====================== START - FINE MANAGEMENT ======================\n");
                    // memberService.fineManagement();
                    System.out.println("====================== END - FINE MANAGEMENT ======================\n");
                    break;
                case "11":
                    System.out.println("====================== START - ISSUE BOOK ======================\n");
                    bookIssueService.issueBook();
                    System.out.println("====================== END - ISSUE BOOK ======================\n");
                    break;
                case "12":
                    System.out.println("====================== START - RETURN BOOK ======================\n");
                    bookIssueService.returnBook();
                    System.out.println("====================== END - RETURN BOOK ======================\n");
                    break;
                case "13":
                    System.out.println("====================== START - RENEW BOOK ======================\n");
                    bookIssueService.renewBook();
                    System.out.println("====================== END - RENEW BOOK ======================\n");
                    break;
                case "14":
                    System.out.println("====================== START - RESERVE BOOK ======================\n");
                    reservationService.reserveBook();
                    System.out.println("====================== END - RESERVE BOOK ======================\n");
                    break;
                case "15":
                    System.out.println("====================== START - VIEW OVERDUE BOOKS ======================\n");
                    bookIssueService.viewOverDueBooks();
                    System.out.println("====================== END - VIEW OVERDUE BOOKS ======================\n");
                    break;
                case "16":
                    System.out.println("====================== START - BOOK RECOMMENDATIONS ======================\n");
                    bookIssueService.bookRecommendations();
                    System.out.println("====================== END - BOOK RECOMMENDATIONS ======================\n");
                    break;
                case "17":
                    System.out.println("====================== START - ADVANCED SEARCH ======================\n");
                    bookIssueService.advancedSearch();
                    System.out.println("====================== END - ADVANCED SEARCH ======================\n");
                    break;
                case "18":
                    System.out.println("====================== START - POPULAR BOOKS ======================\n");
                    bookIssueService.getPopularBooks();
                    System.out.println("====================== END - POPULAR BOOKS ======================\n");
                    break;
                case "19":
                    System.out.println("====================== START - MEMBER ANALYTICS ======================\n");
                    // bookIssueService.getMemberAnalytics();
                    System.out.println("====================== END - MEMBER ANALYTICS ======================\n");
                    break;
                case "20":
                    System.out.println("====================== START - BORROWING REPORTS ======================\n");
                    bookIssueService.borrowingReports();
                    System.out.println("====================== END - BORROWING REPORTS ======================\n");
                    break;
                case "21":
                    System.out
                            .println("====================== START - FINE COLLECTION REPORTS ======================\n");
                    bookIssueService.fineCollectionReports();
                    System.out.println("====================== END - FINE COLLECTION REPORTS ======================\n");
                    break;
                case "22":
                    System.out.println(
                            "====================== START - BOOK POPULARITY ANALYSIS ======================\n");
                    bookIssueService.getPopularBooks();
                    System.out
                            .println("====================== END - BOOK POPULARITY ANALYSIS ======================\n");
                    break;
                case "23":
                    System.out.println(
                            "====================== START - MEMBER ENGAGEMENT REPORTS ======================\n");
                    // bookIssueService.memberEngagementReports();
                    System.out
                            .println("====================== END - MEMBER ENGAGEMENT REPORTS ======================\n");
                    break;
                case "25":
                    System.out.println("====================== START - IMPORT ======================\n");
                    importExportService.importData();
                    System.out.println("====================== END - IMPORT ======================\n");
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
