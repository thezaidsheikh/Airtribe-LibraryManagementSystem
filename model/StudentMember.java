package model;

import common.MemberPolicy;
import common.MemberStatus;

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
        super(name, email, phone, MemberStatus.ACTIVE, "Student");
        this.studentId = studentId;
        this.academicYear = academicYear;
        this.department = department;
    }

    /**
     * Parameterized constructor for StudentMember
     * 
     * @param memberId         The unique identifier for the member
     * @param name             The full name of the student
     * @param email            The email address of the student
     * @param phone            The phone number of the student
     * @param studentId        The student ID or enrollment number
     * @param academicYear     The academic year or semester
     * @param department       The department or course
     * @param membershipStatus The membership status of the member
     */
    public StudentMember(String name, String email, long phone,
            String studentId, String academicYear, String department,
            MemberStatus membershipStatus, long membershipDate, int currentBorrowedBooks, double totalFineAmount,
            int renewalCount) {
        super(name, email, phone, membershipStatus, "Student", membershipDate, currentBorrowedBooks, totalFineAmount,
                renewalCount);
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
     * Returns a string representation of the student member for file storage
     * 
     * @return String representation for persistence
     */
    @Override
    public String toString() {
        return super.toString() + ", studentId=" + studentId + ", academicYear=" + academicYear + ", department="
                + department;
    }
}
