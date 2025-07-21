package common;

/**
 * Defines the policies and rules that apply to different types of library
 * members.
 * This class contains static methods that return various policy values based on
 * member type.
 * The supported member types are: Student, Faculty, and Regular.
 */
public class MemberPolicy {
    /**
     * Gets the maximum number of books a member can borrow at once.
     * 
     * @param type the type of member ("Student", "Faculty", or "Regular")
     * @return the borrowing limit as an integer
     *         3 for Students, 5 for Faculty, 2 for Regular members, 0 for unknown
     *         types
     */
    public static int getBorrowingLimit(String type) {
        switch (type) {
            case "Student":
                return 3; // Students can borrow up to 3 books
            case "Faculty":
                return 5; // Faculty can borrow up to 5 books
            case "Regular":
                return 2; // Regular members can borrow up to 2 books
            default:
                return 0; // Unknown member type - no borrowing allowed
        }
    }

    /**
     * Gets the daily fine amount (in Rs.) for overdue books based on member type.
     * 
     * @param type the type of member ("Student", "Faculty", or "Regular")
     * @return the daily fine amount
     *         Rs. 2.0 for Students, Rs. 1.0 for Faculty, Rs. 3.0 for Regular
     *         members
     */
    public static double getDailyFine(String type) {
        switch (type) {
            case "Student":
                return 2.0; // Rs. 2 per day for Students
            case "Faculty":
                return 1.0; // Rs. 1 per day for Faculty
            case "Regular":
                return 3.0; // Rs. 3 per day for Regular members
            default:
                return 3.0; // Default to Regular member fine for unknown types
        }
    }

    /**
     * Gets the grace period (in days) allowed before fines start accumulating.
     * 
     * @param type the type of member ("Student", "Faculty", or "Regular")
     * @return the grace period in days
     *         3 days for Students, 5 days for Faculty, 2 days for Regular members
     */
    public static int getGracePeriod(String type) {
        switch (type) {
            case "Student":
                return 3; // 3-day grace period for Students
            case "Faculty":
                return 5; // 5-day grace period for Faculty
            case "Regular":
                return 2; // 2-day grace period for Regular members
            default:
                return 0; // No grace period for unknown types
        }
    }

    /**
     * Gets the maximum number of times a book can be renewed by a member.
     * 
     * @param type the type of member ("Student", "Faculty", or "Regular")
     * @return the maximum number of renewals allowed
     *         2 for Students, 3 for Faculty, 1 for Regular members
     */
    public static int getRenewalLimit(String type) {
        switch (type) {
            case "Student":
                return 2; // Students can renew up to 2 times
            case "Faculty":
                return 3; // Faculty can renew up to 3 times
            case "Regular":
                return 1; // Regular members can renew up to 1 time
            default:
                return 0; // No renewals for unknown types
        }
    }

    /**
     * Gets the maximum fine amount (in Rs.) that can be accumulated by a member.
     * 
     * @param type the type of member ("Student", "Faculty", or "Regular")
     * @return the maximum fine amount
     *         Rs. 100.0 for Students, Rs. 50.0 for Faculty, Rs. 200.0 for Regular
     *         members
     */
    public static double getMaxFine(String type) {
        switch (type) {
            case "Student":
                return 100.0; // Maximum fine for Students
            case "Faculty":
                return 50.0; // Maximum fine for Faculty
            case "Regular":
                return 200.0; // Maximum fine for Regular members
            default:
                return 200.0; // Default to Regular member maximum fine
        }
    }

    /**
     * Gets the default due period (in days) for borrowed books.
     * This is the standard loan period before renewal is required.
     * 
     * @return the default due period in days (5 days)
     */
    public static int defaultDueDate() {
        return 5; // Standard loan period for all member types
    }
}
