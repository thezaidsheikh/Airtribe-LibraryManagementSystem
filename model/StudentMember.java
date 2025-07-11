package model;

import common.MemberPolicy;

/**
 * StudentMember class representing a student library member with specific
 * privileges and borrowing rules.
 * Students have moderate borrowing limits and fine policies.
 * 
 * @author Zaid Sheikh
 * @version 1.0
 * @since 2025-01-09
 */
public class StudentMember extends Member {
    private static final long serialVersionUID = 1L;

    /** Student ID or enrollment number */
    private String studentId;

    /** Academic year or semester */
    private String academicYear;

    /** Department or course */
    private String department;

    /**
     * Default constructor for StudentMember
     */
    public StudentMember() {
        super();
        this.memberType = "Student";
    }

    /**
     * Parameterized constructor for StudentMember
     * 
     * @param name         The full name of the student
     * @param email        The email address of the student
     * @param phone        The phone number of the student
     * @param studentId    The student ID or enrollment number
     * @param academicYear The academic year or semester
     * @param department   The department or course
     */
    public StudentMember(String name, String email, long phone,
            String studentId, String academicYear, String department) {
        super(name, email, phone, "Student");
        this.studentId = studentId;
        this.academicYear = academicYear;
        this.department = department;
    }

    /**
     * Gets the student ID
     * 
     * @return The student ID
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Sets the student ID
     * 
     * @param studentId The student ID to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Gets the academic year
     * 
     * @return The academic year
     */
    public String getAcademicYear() {
        return academicYear;
    }

    /**
     * Sets the academic year
     * 
     * @param academicYear The academic year to set
     */
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
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
     * Gets the maximum allowed fine amount before borrowing is suspended
     * 
     * @return The maximum allowed fine amount for students
     */
    @Override
    public double getMaxAllowedFine() {
        return MemberPolicy.getMaxFine(this.memberType);
    }

    /**
     * Calculates fine for overdue books based on student fine policy
     * Students are charged a fixed rate per day for overdue books
     * 
     * @param daysOverdue The number of days the book is overdue
     * @return The fine amount
     */
    @Override
    public double calculateFine(int daysOverdue) {
        if (daysOverdue <= 0) {
            return 0.0;
        }

        double fine = daysOverdue * MemberPolicy.getDailyFine(this.memberType);

        // Apply grace period for first 2 days
        if (daysOverdue <= 2) {
            fine = 0.0;
        } else {
            fine = (daysOverdue - 2) * MemberPolicy.getDailyFine(this.memberType);
        }

        return fine;
    }

    /**
     * Checks if the student can renew books
     * Students can renew books once if they have no overdue books
     * 
     * @return true if student can renew books, false otherwise
     */
    public boolean canRenewBooks() {
        return "Active".equals(membershipStatus) && totalFineAmount == 0.0;
    }

    /**
     * Checks if the student is eligible for extended borrowing during exam periods
     * 
     * @return true if eligible for extended borrowing, false otherwise
     */
    public boolean isEligibleForExtendedBorrowing() {
        return "Active".equals(membershipStatus) &&
                totalFineAmount < (this.getMaxAllowedFine() * 0.5) &&
                currentBorrowedBooks <= (this.getBorrowingLimit() * 0.8);
    }

    /**
     * Gets the student member privileges as a formatted string
     * 
     * @return String describing student privileges
     */
    public String getStudentPrivileges() {
        StringBuilder privileges = new StringBuilder();
        privileges.append("Student Member Privileges:\n");
        privileges.append("- Borrowing Limit: ").append(this.getBorrowingLimit()).append(" books\n");
        privileges.append("- Fine Rate: ₹").append(MemberPolicy.getDailyFine(this.memberType)).append(" per day\n");
        privileges.append("- Grace Period: 2 days\n");
        privileges.append("- Renewal: Once per book\n");
        privileges.append("- Max Fine Limit: ₹").append(this.getMaxAllowedFine()).append("\n");
        return privileges.toString();
    }

    /**
     * Returns a detailed string representation of the student member
     * 
     * @return Detailed string representation
     */
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== STUDENT MEMBER DETAILS ===\n");
        info.append("Member ID: ").append(this.getMemberId()).append("\n");
        info.append("Name: ").append(this.getName()).append("\n");
        info.append("Email: ").append(this.getEmail()).append("\n");
        info.append("Phone: ").append(this.getPhone()).append("\n");
        info.append("Student ID: ").append(this.getStudentId()).append("\n");
        info.append("Department: ").append(this.getDepartment()).append("\n");
        info.append("Academic Year: ").append(this.getAcademicYear()).append("\n");
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
     * Returns a string representation of the student member for file storage
     * 
     * @return String representation for persistence
     */
    @Override
    public String toString() {
        return super.toString() + ", studentId=" + studentId + ", academicYear=" + academicYear + ", department="
                + department + "]";
    }
}
