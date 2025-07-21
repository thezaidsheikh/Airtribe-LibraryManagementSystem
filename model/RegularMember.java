package model;

import common.MemberPolicy;
import common.MemberStatus;

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
     * @param name  The full name of the faculty
     * @param email The email address of the faculty
     * @param phone The phone number of the faculty
     */
    public RegularMember(String name, String email, long phone) {
        super(name, email, phone, MemberStatus.ACTIVE, "Regular");
    }

    /**
     * Parameterized constructor for RegularMember
     * 
     * @param name             The full name of the faculty
     * @param email            The email address of the faculty
     * @param phone            The phone number of the faculty
     * @param membershipStatus The membership status of the member
     */
    public RegularMember(String name, String email, long phone, MemberStatus membershipStatus, long membershipDate,
            int currentBorrowedBooks, double totalFineAmount, int renewalCount) {
        super(name, email, phone, membershipStatus, "Regular", membershipDate, currentBorrowedBooks, totalFineAmount,
                renewalCount);
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
     * Returns a string representation of the faculty member for file storage
     * 
     * @return String representation for persistence
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
