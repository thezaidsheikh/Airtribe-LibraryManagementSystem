package model;

import common.MemberPolicy;

/**
 * FacultyMember class representing a faculty library member with enhanced
 * privileges and borrowing rules.
 * Faculty members have higher borrowing limits and more lenient fine policies.
 * 
 * @author Zaid Sheikh
 * @version 1.0
 * @since 2025-01-09
 */
public class RegularMember extends Member {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for RegularMember
     */
    public RegularMember() {
        super();
        this.memberType = "Regular";
    }

    /**
     * Parameterized constructor for RegularMember
     * 
     * @param memberId The unique identifier for the member
     * @param name     The full name of the faculty
     * @param email    The email address of the faculty
     * @param phone    The phone number of the faculty
     */
    public RegularMember(String name, String email, long phone) {
        super(name, email, phone, "Regular");
    }

    /**
     * Gets the maximum allowed fine amount before borrowing is suspended
     * 
     * @return The maximum allowed fine amount for faculty
     */
    @Override
    public double getMaxAllowedFine() {
        return MemberPolicy.getMaxFine(this.memberType);
    }

    /**
     * Calculates fine for overdue books based on faculty fine policy
     * Faculty members are charged a lower rate per day for overdue books
     * 
     * @param daysOverdue The number of days the book is overdue
     * @return The fine amount
     */
    @Override
    public double calculateFine(int daysOverdue) {
        if (daysOverdue <= 0) {
            return 0.0;
        }

        double fine = 0.0;

        // Apply grace period for first 5 days
        if (daysOverdue <= 5) {
            fine = 0.0;
        } else {
            fine = (daysOverdue - 5) * MemberPolicy.getDailyFine(this.memberType);
        }

        // Apply progressive fine structure
        if (daysOverdue > 30) {
            // Additional penalty for books overdue more than 30 days
            fine += (daysOverdue - 30) * 0.5;
        }

        return fine;
    }

    /**
     * Checks if the faculty can renew books
     * Faculty can renew books multiple times if they have no overdue books
     * 
     * @return true if faculty can renew books, false otherwise
     */
    public boolean canRenewBooks() {
        return "Active".equals(membershipStatus) && totalFineAmount < (this.getMaxAllowedFine() * 0.5);
    }

    /**
     * Gets the maximum number of renewals allowed for faculty
     * 
     * @return The maximum number of renewals
     */
    public int getMaxRenewals() {
        return 3;
    }

    /**
     * Gets the extended borrowing limit for summer/vacation periods
     * 
     * @return The extended borrowing limit
     */
    public int getExtendedBorrowingLimit() {
        return this.getBorrowingLimit() + 5;
    }

    /**
     * Gets the faculty member privileges as a formatted string
     * 
     * @return String describing faculty privileges
     */
    public String getFacultyPrivileges() {
        StringBuilder privileges = new StringBuilder();
        privileges.append("Faculty Member Privileges:\n");
        privileges.append("- Borrowing Limit: ").append(this.getBorrowingLimit()).append(" books\n");
        privileges.append("- Fine Rate: ₹").append(MemberPolicy.getDailyFine(this.memberType)).append(" per day\n");
        privileges.append("- Grace Period: 5 days\n");
        privileges.append("- Renewals: Up to ").append(this.getMaxRenewals()).append(" times\n");
        privileges.append("- Max Fine Limit: ₹").append(this.getMaxAllowedFine()).append("\n");
        return privileges.toString();
    }

    /**
     * Returns a detailed string representation of the faculty member
     * 
     * @return Detailed string representation
     */
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== FACULTY MEMBER DETAILS ===\n");
        info.append("Member ID: ").append(this.getMemberId()).append("\n");
        info.append("Name: ").append(this.getName()).append("\n");
        info.append("Email: ").append(this.getEmail()).append("\n");
        info.append("Phone: ").append(this.getPhone()).append("\n");
        info.append("Membership Date: ").append(this.getFormattedMembershipDate()).append("\n");
        info.append("Member Type: ").append(this.getMemberType()).append("\n");
        info.append("Borrowing Limit: ").append(this.getBorrowingLimit()).append("\n");
        info.append("Current Borrowed Books: ").append(this.getCurrentBorrowedBooks()).append("\n");
        info.append("Available Capacity: ").append(this.getAvailableBorrowingCapacity()).append("\n");
        info.append("Total Fine Amount: ₹").append(String.format("%.2f", this.getTotalFineAmount())).append("\n");
        info.append("Membership Status: ").append(this.getMembershipStatus()).append("\n");
        info.append("Can Borrow Books: ").append(this.canBorrowBooks() ? "Yes" : "No").append("\n");
        info.append("Can Renew Books: ").append(this.canRenewBooks() ? "Yes" : "No").append("\n");
        return info.toString();
    }

    /**
     * Returns a string representation of the faculty member for file storage
     * 
     * @return String representation for persistence
     */
    @Override
    public String toString() {
        return super.toString() + "]";
    }
}
