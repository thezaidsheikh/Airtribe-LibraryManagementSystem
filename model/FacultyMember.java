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
