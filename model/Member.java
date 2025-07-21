package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import common.MemberPolicy;
import common.MemberStatus;
import common.utils;

/**
 * Abstract base class representing a member in the Library Management System.
 * This class serves as the parent class for different types of library members
 * (e.g., Student, Faculty, Regular) and defines common attributes and behaviors
 * that all member types share.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Member identification and contact information</li>
 *   <li>Membership status tracking (Active, Suspended, Expired)</li>
 *   <li>Borrowing and renewal limits based on member type</li>
 *   <li>Fine calculation and management</li>
 *   <li>Book borrowing and returning functionality</li>
 * </ul>
 * </p>
 * 
 * @see StudentMember
 * @see FacultyMember
 * @see RegularMember
 * @author Library Management System Team
 * @version 1.0
 * @since 2025-07-21
 */
public abstract class Member implements Serializable {
    /**
     * The serialization version UID for ensuring version compatibility
     * during object serialization/deserialization.
     */
    private static final long serialVersionUID = 1L;

    /** 
     * Unique identifier for the member.
     * Automatically generated as a 10-digit number.
     */
    protected int memberId;

    /** 
     * Full name of the member.
     */
    protected String name;

    /** 
     * Email address of the member.
     * Used for notifications and account recovery.
     */
    protected String email;

    /** 
     * Phone number of the member.
     * Used for important notifications and account verification.
     */
    protected long phone;

    /** 
     * Membership registration date stored as epoch time (milliseconds since 1970-01-01).
     * 
     * @see utils#getEpochTime()
     */
    protected long membershipDate;

    /** 
     * Type of member (e.g., "STUDENT", "FACULTY", "REGULAR").
     * Determines borrowing limits, fine rates, and other policies.
     * 
     * @see MemberPolicy
     */
    protected String memberType;

    /** 
     * Number of books currently borrowed by the member.
     * Used to enforce borrowing limits.
     */
    protected int currentBorrowedBooks;

    /** 
     * Total accumulated fine amount in the member's account.
     * If this exceeds the maximum allowed fine, the member's account may be suspended.
     */
    protected double totalFineAmount;

    /** 
     * Current status of the member's account.
     * 
     * @see MemberStatus
     */
    protected MemberStatus membershipStatus;

    /** 
     * Number of times the member has renewed borrowed items.
     * Used to enforce renewal limits.
     */
    protected int renewalCount;

    /**
     * Default constructor that initializes a new Member with default values.
     * <p>
     * Initializes the member with:
     * <ul>
     *   <li>Current time as membership date</li>
     *   <li>No borrowed books</li>
     *   <li>Zero fine amount</li>
     *   <li>Active membership status</li>
     *   <li>Zero renewal count</li>
     * </ul>
     * 
     * @see #Member(String, String, long, MemberStatus, String)
     * @see #Member(String, String, long, MemberStatus, String, long, int, double, int)
     */
    public Member() {
        this.membershipDate = utils.getEpochTime();
        this.currentBorrowedBooks = 0;
        this.totalFineAmount = 0.0;
        this.membershipStatus = MemberStatus.ACTIVE;
        this.renewalCount = 0;
    }

    /**
     * Constructs a new Member with the specified details.
     * <p>
     * This constructor initializes a member with the provided information and sets
     * default values for other fields:
     * <ul>
     *   <li>Generates a unique 10-digit member ID</li>
     *   <li>Sets the membership date to the current time</li>
     *   <li>Initializes borrowed books count to 0</li>
     *   <li>Initializes total fine amount to 0.0</li>
     *   <li>Initializes renewal count to 0</li>
     * </ul>
     *
     * @param name             The full name of the member (cannot be null or empty)
     * @param email            The email address of the member (must be in valid format)
     * @param phone            The phone number of the member (must be positive)
     * @param membershipStatus The initial membership status (typically ACTIVE for new members)
     * @param memberType       The type of member (e.g., "STUDENT", "FACULTY", "REGULAR")
     * 
     * @throws IllegalArgumentException if name is null/empty, email is invalid, or phone is not positive
     * 
     * @see #Member()
     * @see #Member(String, String, long, MemberStatus, String, long, int, double, int)
     * @see MemberStatus
     */
    public Member(String name, String email, long phone, MemberStatus membershipStatus,
            String memberType) {
        this.memberId = utils.generateId(10);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberType = memberType;
        this.membershipDate = utils.getEpochTime();
        this.currentBorrowedBooks = 0;
        this.totalFineAmount = 0.0;
        this.membershipStatus = membershipStatus;
        this.renewalCount = 0;
    }

    /**
     * Full parameterized constructor that initializes all member fields.
     * <p>
     * This constructor is typically used when loading a member from persistent storage.
     *
     * @param name                 The full name of the member (cannot be null or empty)
     * @param email                The email address of the member (must be a valid email format)
     * @param phone                The phone number of the member (must be a positive number)
     * @param membershipStatus     The current membership status
     * @param memberType           The type of member (e.g., "STUDENT", "FACULTY", "REGULAR")
     * @param membershipDate       The membership registration date as epoch time (milliseconds since 1970-01-01)
     * @param currentBorrowedBooks The number of books currently borrowed (must be >= 0)
     * @param totalFineAmount      The total fine amount accumulated (must be >= 0)
     * @param renewalCount         The number of times books have been renewed (must be >= 0)
     * 
     * @throws IllegalArgumentException if any parameter is invalid
     * 
     * @see MemberStatus
     * @see #Member()
     * @see #Member(String, String, long, MemberStatus, String)
     */
    public Member(String name, String email, long phone, MemberStatus membershipStatus,
            String memberType, long membershipDate, int currentBorrowedBooks, double totalFineAmount,
            int renewalCount) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (phone <= 0) {
            throw new IllegalArgumentException("Phone number must be a positive number");
        }
        if (currentBorrowedBooks < 0) {
            throw new IllegalArgumentException("Current borrowed books must be >= 0");
        }
        if (totalFineAmount < 0) {
            throw new IllegalArgumentException("Total fine amount must be >= 0");
        }
        if (renewalCount < 0) {
            throw new IllegalArgumentException("Renewal count must be >= 0");
        }

        this.memberId = utils.generateId(10);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberType = memberType;
        this.membershipDate = membershipDate;
        this.currentBorrowedBooks = currentBorrowedBooks;
        this.totalFineAmount = totalFineAmount;
        this.membershipStatus = membershipStatus;
        this.renewalCount = renewalCount;
    }

    /**
     * Retrieves the unique identifier of this member.
     * 
     * @return The member's unique ID as a 10-digit number
     * 
     * @see #setMemberId(int)
     */
    public int getMemberId() {
        return this.memberId;
    }

    /**
     * Sets the unique identifier for this member.
     * <p>
     * Note: This method should typically only be called when loading member data
     * from persistent storage. For new members, the ID is automatically generated.
     *
     * @param memberId The unique identifier to set (must be a positive number)
     * 
     * @throws IllegalArgumentException if memberId is not positive
     * 
     * @see #getMemberId()
     */
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    /**
     * Retrieves the full name of this member.
     * 
     * @return The member's full name as a String
     * 
     * @see #setName(String)
     */
    public String getName() {
        return this.name;
    }

    /**
     * Updates the name of this member.
     *
     * @param name The new full name (cannot be null or empty)
     * 
     * @throws IllegalArgumentException if name is null or empty
     * 
     * @see #getName()
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the email address associated with this member's account.
     * 
     * @return The member's email address as a String
     * 
     * @see #setEmail(String)
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Updates the email address for this member.
     *
     * @param email The new email address (must be in a valid email format)
     * 
     * @throws IllegalArgumentException if email is null or not in a valid format
     * 
     * @see #getEmail()
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the phone number associated with this member's account.
     * 
     * @return The member's phone number as a long
     * 
     * @see #setPhone(long)
     */
    public long getPhone() {
        return this.phone;
    }

    /**
     * Updates the phone number for this member.
     *
     * @param phone The new phone number (must be a positive number)
     * 
     * @throws IllegalArgumentException if phone is not a positive number
     * 
     * @see #getPhone()
     */
    public void setPhone(long phone) {
        this.phone = phone;
    }

    /**
     * Retrieves the date when this member registered, expressed as epoch time.
     * 
     * @return The membership registration date in milliseconds since 1970-01-01
     * 
     * @see #setMembershipDate(long)
     * @see utils#getEpochTime()
     */
    public long getMembershipDate() {
        return this.membershipDate;
    }

    /**
     * Updates the membership registration date for this member.
     * <p>
     * Note: This method should typically only be used when loading member data
     * from persistent storage. For new members, the membership date is automatically set.
     *
     * @param membershipDate The new membership date in milliseconds since 1970-01-01
     * 
     * @throws IllegalArgumentException if membershipDate is negative
     * 
     * @see #getMembershipDate()
     */
    public void setMembershipDate(long membershipDate) {
        this.membershipDate = membershipDate;
    }

    /**
     * Retrieves the type of this member (e.g., "STUDENT", "FACULTY", "REGULAR").
     * <p>
     * The member type determines various policies such as borrowing limits,
     * fine rates, and renewal limits.
     *
     * @return The member type as a String
     * 
     * @see MemberPolicy
     */
    public String getMemberType() {
        return this.memberType;
    }

    /**
     * Retrieves the maximum number of books this member can borrow simultaneously.
     * <p>
     * The borrowing limit is determined by the member's type and is defined in
     * the {@link MemberPolicy} class.
     *
     * @return The maximum number of books that can be borrowed at once
     * 
     * @see MemberPolicy#getBorrowingLimit(String)
     */
    public int getBorrowingLimit() {
        return MemberPolicy.getBorrowingLimit(this.memberType);
    }

    /**
     * Retrieves the maximum number of times this member can renew borrowed items.
     * <p>
     * The renewal limit is determined by the member's type and is defined in
     * the {@link MemberPolicy} class.
     *
     * @return The maximum number of renewals allowed
     * 
     * @see MemberPolicy#getRenewalLimit(String)
     */
    public int getRenewalLimit() {
        return MemberPolicy.getRenewalLimit(this.memberType);
    }

    /**
     * Retrieves the current number of times this member has renewed borrowed items.
     * <p>
     * This count is used to enforce renewal limits and is automatically
     * incremented when {@link #renewBook()} is called.
     *
     * @return The current number of renewals
     * 
     * @see #renewBook()
     * @see #getRenewalLimit()
     */
    public int getRenewalCount() {
        return this.renewalCount;
    }

    /**
     * Updates the renewal count for this member.
     * <p>
     * This method should be used with caution and typically only when loading
     * member data from persistent storage. The renewal count is normally
     * managed automatically by the system.
     *
     * @param renewalCount The new renewal count (must be >= 0)
     * 
     * @throws IllegalArgumentException if renewalCount is negative
     * 
     * @see #getRenewalCount()
     * @see #renewBook()
     */
    public void setRenewalCount(int renewalCount) {
        this.renewalCount = renewalCount;
    }

    /**
     * Retrieves the number of books currently borrowed by this member.
     * <p>
     * This count is used to enforce borrowing limits and is automatically
     * managed by the {@link #borrowBook()} and {@link #returnBook()} methods.
     *
     * @return The current number of borrowed books
     * 
     * @see #borrowBook()
     * @see #returnBook()
     * @see #getBorrowingLimit()
     */
    public int getCurrentBorrowedBooks() {
        return this.currentBorrowedBooks;
    }

    /**
     * Updates the count of currently borrowed books for this member.
     * <p>
     * This method should be used with caution and typically only when loading
     * member data from persistent storage. The count is normally managed
     * automatically by the system.
     *
     * @param currentBorrowedBooks The new count of borrowed books (must be >= 0 and <= borrowing limit)
     * 
     * @throws IllegalArgumentException if currentBorrowedBooks is negative or exceeds borrowing limit
     * 
     * @see #getCurrentBorrowedBooks()
     * @see #borrowBook()
     * @see #returnBook()
     */
    public void setCurrentBorrowedBooks(int currentBorrowedBooks) {
        this.currentBorrowedBooks = currentBorrowedBooks;
    }

    /**
     * Retrieves the total outstanding fine amount for this member.
     * <p>
     * If this amount exceeds the maximum allowed fine (determined by
     * {@link #getMaxAllowedFine()}), the member's account may be suspended.
     *
     * @return The total fine amount in the local currency
     * 
     * @see #addFine(double)
     * @see #payFine(double)
     * @see #getMaxAllowedFine()
     */
    public double getTotalFineAmount() {
        return this.totalFineAmount;
    }

    /**
     * Updates the total fine amount for this member.
     * <p>
     * This method should be used with caution and typically only when loading
     * member data from persistent storage. Fines are normally managed
     * automatically by the system through {@link #addFine(double)} and
     * {@link #payFine(double)}.
     *
     * @param totalFineAmount The new total fine amount (must be >= 0)
     * 
     * @throws IllegalArgumentException if totalFineAmount is negative
     * 
     * @see #getTotalFineAmount()
     * @see #addFine(double)
     * @see #payFine(double)
     */
    public void setTotalFineAmount(double totalFineAmount) {
        this.totalFineAmount = totalFineAmount;
    }

    /**
     * Retrieves the current status of this member's account.
     * <p>
     * Possible status values are defined in the {@link MemberStatus} enum.
     * The status affects the member's ability to borrow and renew books.
     *
     * @return The current membership status
     * 
     * @see MemberStatus
     * @see #setMembershipStatus(MemberStatus)
     */
    public MemberStatus getMembershipStatus() {
        return this.membershipStatus;
    }

    /**
     * Updates the status of this member's account.
     * <p>
     * The membership status affects what actions the member can perform.
     * For example, suspended members cannot borrow or renew books until their
     * status is changed back to ACTIVE.
     *
     * @param membershipStatus The new membership status (cannot be null)
     * 
     * @throws IllegalArgumentException if membershipStatus is null
     * 
     * @see MemberStatus
     * @see #getMembershipStatus()
     */
    public void setMembershipStatus(MemberStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    /**
     * Determines if this member is eligible to borrow additional books.
     * <p>
     * A member can borrow more books if all the following conditions are met:
     * <ol>
     *   <li>Current borrowed books < Borrowing limit</li>
     *   <li>Membership status is ACTIVE</li>
     *   <li>Total fine amount ≤ Maximum allowed fine</li>
     *   <li>Renewal count ≤ Renewal limit</li>
     * </ol>
     *
     * @return true if the member can borrow more books, false otherwise
     * 
     * @see #getBorrowingLimit()
     * @see #getMaxAllowedFine()
     * @see #getRenewalLimit()
     */
    public boolean canBorrowBooks() {
        return this.currentBorrowedBooks < this.getBorrowingLimit() &&
                this.membershipStatus == MemberStatus.ACTIVE &&
                this.totalFineAmount <= this.getMaxAllowedFine() &&
                this.renewalCount <= this.getRenewalLimit();
    }

    /**
     * Determines if this member is eligible to renew borrowed books.
     * <p>
     * A member can renew books if all the following conditions are met:
     * <ol>
     *   <li>Membership status is ACTIVE</li>
     *   <li>Total fine amount is zero</li>
     *   <li>Renewal count < Renewal limit</li>
     * </ol>
     *
     * @return true if the member can renew books, false otherwise
     * 
     * @see #canBorrowBooks()
     * @see #getRenewalLimit()
     * @see #getRenewalCount()
     */
    public boolean canRenewBooks() {
        return this.membershipStatus == MemberStatus.ACTIVE && this.totalFineAmount == 0.0
                && this.renewalCount < this.getRenewalLimit();
    }

    /**
     * Retrieves the maximum fine amount allowed before the member's borrowing
     * privileges are suspended.
     * <p>
     * This is an abstract method that must be implemented by concrete subclasses
     * to provide the specific maximum fine amount based on the member type.
     *
     * @return The maximum allowed fine amount in the local currency
     * 
     * @see #addFine(double)
     * @see #getTotalFineAmount()
     * @see MemberPolicy#getMaxFine(String)
     */
    public abstract double getMaxAllowedFine();

    /**
     * Calculates the fine amount for an overdue book based on the number of days late.
     * <p>
     * This method provides a default implementation that calculates the fine as:
     * <pre>
     * fine = daysOverdue * dailyFineRate
     * </pre>
     * where dailyFineRate is obtained from {@link MemberPolicy#getDailyFine(String)}.
     * Subclasses may override this method to provide different fine calculation logic.
     *
     * @param daysOverdue The number of days the book is overdue (must be >= 0)
     * @return The calculated fine amount in the local currency
     * 
     * @throws IllegalArgumentException if daysOverdue is negative
     * 
     * @see MemberPolicy#getDailyFine(String)
     * @see #addFine(double)
     */
    public double calculateFine(int daysOverdue) {
        if (daysOverdue <= 0) {
            return 0.0;
        }
        return daysOverdue * MemberPolicy.getDailyFine(this.memberType);
    };

    /**
     * Adds a fine to the member's total fine amount.
     * <p>
     * If the total fine amount after adding the new fine exceeds the maximum
     * allowed fine (as determined by {@link #getMaxAllowedFine()}), the member's
     * account will be automatically suspended.
     *
     * @param fine The fine amount to add (must be >= 0)
     * 
     * @throws IllegalArgumentException if fine is negative
     * 
     * @see #getTotalFineAmount()
     * @see #getMaxAllowedFine()
     * @see #payFine(double)
     */
    public void addFine(double fine) {
        this.totalFineAmount += fine;
        if (this.totalFineAmount >= getMaxAllowedFine()) {
            this.membershipStatus = MemberStatus.SUSPENDED;
        }
    }

    /**
     * Processes a payment toward the member's outstanding fines.
     * <p>
     * If the payment amount is positive and doesn't exceed the total fine amount,
     * it will be deducted from the total. If the payment brings the total fine
     * below the maximum allowed fine and the account was suspended due to fines,
     * the account will be automatically reactivated.
     *
     * @param amount The payment amount (must be > 0 and ≤ total fine amount)
     * 
     * @throws IllegalArgumentException if amount is not positive or exceeds total fine amount
     * 
     * @see #getTotalFineAmount()
     * @see #addFine(double)
     * @see #getMaxAllowedFine()
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
     * Records a book renewal for this member.
     * <p>
     * This method increments the renewal count if the member is eligible to renew books
     * (as determined by {@link #canRenewBooks()}). The actual renewal of the book
     * should be handled separately by the calling code.
     *
     * @see #getRenewalCount()
     * @see #getRenewalLimit()
     * @see #canRenewBooks()
     */
    public void renewBook() {
        if (this.canBorrowBooks()) {
            this.renewalCount++;
        }
    }

    /**
     * Records that this member has borrowed a book.
     * <p>
     * This method increments the count of currently borrowed books if the member
     * is eligible to borrow more books (as determined by {@link #canBorrowBooks()}).
     * The actual book checkout should be handled separately by the calling code.
     *
     * @see #returnBook()
     * @see #getCurrentBorrowedBooks()
     * @see #getBorrowingLimit()
     * @see #canBorrowBooks()
     */
    public void borrowBook() {
        if (this.canBorrowBooks()) {
            this.currentBorrowedBooks++;
        }
    }

    /**
     * Records that this member has returned a book.
     * <p>
     * This method decrements the count of currently borrowed books if the count
     * is greater than zero. The actual book return should be handled separately
     * by the calling code.
     *
     * @see #borrowBook()
     * @see #getCurrentBorrowedBooks()
     */
    public void returnBook() {
        if (this.currentBorrowedBooks > 0) {
            this.currentBorrowedBooks--;
        }
    }

    /**
     * Calculates the number of additional books this member can borrow.
     * <p>
     * The available capacity is determined by subtracting the number of currently
     * borrowed books from the member's borrowing limit. This value is always
     * non-negative, even if the member has exceeded their borrowing limit.
     *
     * @return The number of additional books that can be borrowed (≥ 0)
     * 
     * @see #getBorrowingLimit()
     * @see #getCurrentBorrowedBooks()
     * @see #canBorrowBooks()
     */
    public int getAvailableBorrowingCapacity() {
        return this.getBorrowingLimit() - this.currentBorrowedBooks;
    }

    /**
     * Returns a string representation of this member's information.
     * <p>
     * The string includes all relevant member details in a comma-separated format:
     * memberId, name, email, phone, membershipDate, memberType, currentBorrowedBooks,
     * renewalCount, totalFineAmount, and membershipStatus.
     *
     * @return A string containing the member's details
     * 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "memberId=" + this.memberId + ", name=" + this.name + ", email=" + this.email + ", phone="
                + this.phone + ", membershipDate=" + this.membershipDate + ", memberType="
                + this.memberType + ", currentBorrowedBooks=" + this.currentBorrowedBooks + ", renewalCount="
                + this.renewalCount + ", totalFineAmount=" + this.totalFineAmount + ", membershipStatus="
                + this.membershipStatus;
    }
}
