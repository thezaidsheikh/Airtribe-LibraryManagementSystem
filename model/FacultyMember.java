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
public class FacultyMember extends Member {
    private static final long serialVersionUID = 1L;

    /** Faculty ID or employee number */
    private String facultyId;

    /** Department or faculty */
    private String department;

    /** Designation or position */
    private String designation;

    /**
     * Default constructor for FacultyMember
     */
    public FacultyMember() {
        super();
        this.memberType = "Faculty";
    }

    /**
     * Parameterized constructor for FacultyMember
     * 
     * @param memberId    The unique identifier for the member
     * @param name        The full name of the faculty
     * @param email       The email address of the faculty
     * @param phone       The phone number of the faculty
     * @param facultyId   The faculty ID or employee number
     * @param department  The department or faculty
     * @param designation The designation or position
     */
    public FacultyMember(String name, String email, long phone,
            String facultyId, String department, String designation) {
        super(name, email, phone, "Faculty");
        this.facultyId = facultyId;
        this.department = department;
        this.designation = designation;
    }

    /**
     * Constructor with custom borrowing limit
     * 
     * @param memberId             The unique identifier for the member
     * @param name                 The full name of the faculty
     * @param email                The email address of the faculty
     * @param phone                The phone number of the faculty
     * @param facultyId            The faculty ID or employee number
     * @param department           The department or faculty
     * @param designation          The designation or position
     * @param customBorrowingLimit Custom borrowing limit for this faculty
     */
    public FacultyMember(String name, String email, long phone,
            String facultyId, String department, String designation,
            int customBorrowingLimit) {
        super(name, email, phone, "Faculty");
        this.facultyId = facultyId;
        this.department = department;
        this.designation = designation;
    }

    /**
     * Gets the faculty ID
     * 
     * @return The faculty ID
     */
    public String getFacultyId() {
        return facultyId;
    }

    /**
     * Sets the faculty ID
     * 
     * @param facultyId The faculty ID to set
     */
    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    /**
     * Gets the department
     * 
     * @return The department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department
     * 
     * @param department The department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the designation
     * 
     * @return The designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the designation
     * 
     * @param designation The designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
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
     * Checks if the faculty is eligible for special research collection access
     * 
     * @return true if eligible for special collection access, false otherwise
     */
    public boolean isEligibleForSpecialCollection() {
        return "Active".equals(membershipStatus) &&
                totalFineAmount == 0.0 &&
                ("Professor".equals(designation) ||
                        "Associate Professor".equals(designation) ||
                        "Assistant Professor".equals(designation));
    }

    /**
     * Checks if the faculty can request inter-library loans
     * 
     * @return true if eligible for inter-library loans, false otherwise
     */
    public boolean canRequestInterLibraryLoans() {
        return "Active".equals(membershipStatus) &&
                totalFineAmount < (this.getMaxAllowedFine() * 0.3);
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
        privileges.append("- Renewals: Up to ").append(getMaxRenewals()).append(" times\n");
        privileges.append("- Max Fine Limit: ₹").append(this.getMaxAllowedFine()).append("\n");
        privileges.append("- Special Collection Access: ").append(isEligibleForSpecialCollection() ? "Yes" : "No")
                .append("\n");
        privileges.append("- Inter-Library Loans: ").append(canRequestInterLibraryLoans() ? "Yes" : "No").append("\n");
        privileges.append("- Extended Summer Limit: ").append(getExtendedBorrowingLimit()).append(" books\n");
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
        info.append("Member ID: ").append(getMemberId()).append("\n");
        info.append("Name: ").append(getName()).append("\n");
        info.append("Email: ").append(getEmail()).append("\n");
        info.append("Phone: ").append(getPhone()).append("\n");
        info.append("Faculty ID: ").append(getFacultyId()).append("\n");
        info.append("Department: ").append(getDepartment()).append("\n");
        info.append("Designation: ").append(getDesignation()).append("\n");
        info.append("Membership Date: ").append(getFormattedMembershipDate()).append("\n");
        info.append("Member Type: ").append(getMemberType()).append("\n");
        info.append("Borrowing Limit: ").append(getBorrowingLimit()).append("\n");
        info.append("Current Borrowed Books: ").append(getCurrentBorrowedBooks()).append("\n");
        info.append("Available Capacity: ").append(getAvailableBorrowingCapacity()).append("\n");
        info.append("Total Fine Amount: ₹").append(String.format("%.2f", getTotalFineAmount())).append("\n");
        info.append("Membership Status: ").append(getMembershipStatus()).append("\n");
        info.append("Can Borrow Books: ").append(canBorrowBooks() ? "Yes" : "No").append("\n");
        info.append("Can Renew Books: ").append(canRenewBooks() ? "Yes" : "No").append("\n");
        info.append("Special Collection Access: ").append(isEligibleForSpecialCollection() ? "Yes" : "No").append("\n");
        return info.toString();
    }

    /**
     * Returns a string representation of the faculty member for file storage
     * 
     * @return String representation for persistence
     */
    @Override
    public String toString() {
        return super.toString() + ", facultyId=" + facultyId + ", department=" + department + ", designation="
                + designation + "]";
    }
}
