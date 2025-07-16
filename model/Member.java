package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import common.MemberPolicy;
import common.MemberStatus;
import common.utils;

/**
 * Base Member class representing a library member with common attributes and
 * functionality.
 * This class serves as the parent class for different types of library members.
 * 
 */
public abstract class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Unique identifier for the member */
    protected int memberId;

    /** Full name of the member */
    protected String name;

    /** Email address of the member */
    protected String email;

    /** Phone number of the member */
    protected long phone;

    /** Membership registration date as epoch time */
    protected long membershipDate;

    /** Type of member (Student, Faculty, General Public) */
    protected String memberType;

    /** Number of books currently borrowed */
    protected int currentBorrowedBooks;

    /** Total accumulated fine amount */
    protected double totalFineAmount;

    /** Membership status (Active, Suspended, Expired) */
    protected MemberStatus membershipStatus;

    /** Number of times the member has renewed books */
    protected int renewalCount;

    /**
     * Default constructor for Member class
     */
    public Member() {
        this.membershipDate = utils.getEpochTime();
        this.currentBorrowedBooks = 0;
        this.totalFineAmount = 0.0;
        this.membershipStatus = MemberStatus.ACTIVE;
        this.renewalCount = 0;
    }

    /**
     * Parameterized constructor for Member class
     * 
     * @param name       The full name of the member
     * @param email      The email address of the member
     * @param phone      The phone number of the member
     * @param memberType The type of member (Student, Faculty, General Public)
     */
    public Member(String name, String email, long phone,
            String memberType) {
        this.memberId = utils.generateId(10);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberType = memberType;
        this.membershipDate = utils.getEpochTime();
        this.currentBorrowedBooks = 0;
        this.totalFineAmount = 0.0;
        this.membershipStatus = MemberStatus.ACTIVE;
        this.renewalCount = 0;
    }

    /**
     * Gets the member ID
     * 
     * @return The member ID
     */
    public int getMemberId() {
        return this.memberId;
    }

    /**
     * Sets the member ID
     * 
     * @param memberId The member ID to set
     */
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    /**
     * Gets the member name
     * 
     * @return The member name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the member name
     * 
     * @param name The member name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the member email
     * 
     * @return The member email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the member email
     * 
     * @param email The member email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the member phone number
     * 
     * @return The member phone number
     */
    public long getPhone() {
        return this.phone;
    }

    /**
     * Sets the member phone number
     * 
     * @param phone The member phone number to set
     */
    public void setPhone(long phone) {
        this.phone = phone;
    }

    /**
     * Gets the membership date as epoch time
     * 
     * @return The membership date
     */
    public long getMembershipDate() {
        return this.membershipDate;
    }

    /**
     * Sets the membership date
     * 
     * @param membershipDate The membership date to set
     */
    public void setMembershipDate(long membershipDate) {
        this.membershipDate = membershipDate;
    }

    /**
     * Gets the member type
     * 
     * @return The member type
     */
    public String getMemberType() {
        return this.memberType;
    }

    /**
     * Gets the borrowing limit for this member
     * 
     * @return The borrowing limit
     */
    public int getBorrowingLimit() {
        return MemberPolicy.getBorrowingLimit(this.memberType);
    }

    /**
     * Gets the renewal limit for this member
     * 
     * @return The renewal limit
     */
    public int getRenewalLimit() {
        return MemberPolicy.getRenewalLimit(this.memberType);
    }

    /**
     * Gets the renewal count for this member
     * 
     * @return The renewal count
     */
    public int getRenewalCount() {
        return this.renewalCount;
    }

    /**
     * Sets the renewal count
     * 
     * @param renewalCount The renewal count to set
     */
    public void setRenewalCount(int renewalCount) {
        this.renewalCount = renewalCount;
    }

    /**
     * Gets the current number of borrowed books
     * 
     * @return The current borrowed books count
     */
    public int getCurrentBorrowedBooks() {
        return this.currentBorrowedBooks;
    }

    /**
     * Sets the current number of borrowed books
     * 
     * @param currentBorrowedBooks The current borrowed books count to set
     */
    public void setCurrentBorrowedBooks(int currentBorrowedBooks) {
        this.currentBorrowedBooks = currentBorrowedBooks;
    }

    /**
     * Gets the total fine amount
     * 
     * @return The total fine amount
     */
    public double getTotalFineAmount() {
        return this.totalFineAmount;
    }

    /**
     * Sets the total fine amount
     * 
     * @param totalFineAmount The total fine amount to set
     */
    public void setTotalFineAmount(double totalFineAmount) {
        this.totalFineAmount = totalFineAmount;
    }

    /**
     * Gets the membership status
     * 
     * @return The membership status
     */
    public MemberStatus getMembershipStatus() {
        return this.membershipStatus;
    }

    /**
     * Sets the membership status
     * 
     * @param membershipStatus The membership status to set
     */
    public void setMembershipStatus(MemberStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    /**
     * Checks if the member can borrow more books
     * 
     * @return true if member can borrow more books, false otherwise
     */
    public boolean canBorrowBooks() {
        return this.currentBorrowedBooks < this.getBorrowingLimit() &&
                this.membershipStatus == MemberStatus.ACTIVE &&
                this.totalFineAmount <= this.getMaxAllowedFine() &&
                this.renewalCount <= this.getRenewalLimit();
    }

    /**
     * Checks if the member can renew books
     * 
     * @return true if member can renew books, false otherwise
     */
    public boolean canRenewBooks() {
        return this.membershipStatus == MemberStatus.ACTIVE && this.totalFineAmount == 0.0
                && this.renewalCount < this.getRenewalLimit();
    }

    /**
     * Gets the maximum allowed fine amount before borrowing is suspended
     * This is abstract and will be implemented by subclasses
     * 
     * @return The maximum allowed fine amount
     */
    public abstract double getMaxAllowedFine();

    /**
     * Calculates fine for overdue books
     * This is abstract and will be implemented by subclasses
     * 
     * @param daysOverdue The number of days the book is overdue
     * @return The fine amount
     */
    public double calculateFine(int daysOverdue) {
        if (daysOverdue <= 0) {
            return 0.0;
        }
        return daysOverdue * MemberPolicy.getDailyFine(this.memberType);
    };

    /**
     * Adds fine to the member's total fine amount
     * 
     * @param fine The fine amount to add
     */
    public void addFine(double fine) {
        this.totalFineAmount += fine;
        if (this.totalFineAmount >= getMaxAllowedFine()) {
            this.membershipStatus = MemberStatus.SUSPENDED;
        }
    }

    /**
     * Pays fine and reduces the total fine amount
     * 
     * @param amount The amount to pay
     */
    public void payFine(double amount) {
        if (amount > 0 && amount <= this.totalFineAmount) {
            this.totalFineAmount -= amount;
            if (this.totalFineAmount < getMaxAllowedFine() && this.membershipStatus == MemberStatus.SUSPENDED) {
                this.membershipStatus = MemberStatus.ACTIVE;
            }
        }
    }

    /**
     * Increments the renewal count
     */
    public void renewBook() {
        if (this.canBorrowBooks()) {
            this.renewalCount++;
        }
    }

    /**
     * Increments the current borrowed books count
     */
    public void borrowBook() {
        if (this.canBorrowBooks()) {
            this.currentBorrowedBooks++;
        }
    }

    /**
     * Decrements the current borrowed books count
     */
    public void returnBook() {
        if (this.currentBorrowedBooks > 0) {
            this.currentBorrowedBooks--;
        }
    }

    /**
     * Gets the membership date as a formatted string
     * 
     * @return The formatted membership date
     */
    public String getFormattedMembershipDate() {
        LocalDate date = utils.convertEpochToDate(this.membershipDate);
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Gets the available borrowing capacity
     * 
     * @return The number of books that can still be borrowed
     */
    public int getAvailableBorrowingCapacity() {
        return this.getBorrowingLimit() - this.currentBorrowedBooks;
    }

    /**
     * Returns a string representation of the member
     * 
     * @return String representation of the member
     */
    @Override
    public String toString() {
        return "Member [memberId=" + this.memberId + ", name=" + this.name + ", email=" + this.email + ", phone="
                + this.phone + ", membershipDate=" + this.getFormattedMembershipDate() + ", memberType="
                + this.memberType + ", currentBorrowedBooks=" + this.currentBorrowedBooks + ", renewalCount="
                + this.renewalCount + ", totalFineAmount=" + this.totalFineAmount + ", membershipStatus="
                + this.membershipStatus;
    }
}
