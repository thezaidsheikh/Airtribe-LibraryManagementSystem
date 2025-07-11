package model;

import common.utils;

public class BookIssue {
    private static final long serialVersionUID = 1L;

    /** Unique identifier for the book issue */
    private long issueId;

    /** The member who issued the book */
    private Member member;

    /** The book that was issued */
    private Book book;

    /** The date when the book was issued */
    private long issueDate;

    /** The date when the book is due to be returned */
    private long dueDate;

    /** The date when the book was returned */
    private long returnDate;

    /** The fine amount for the book issue */
    private double fineAmount;

    /**
     * Constructs a BookIssue instance with the given member, book, and fine amount.
     * 
     * @param member
     * @param book
     * @param fineAmount
     */
    public BookIssue(Member member, Book book, double fineAmount) {
        this.issueId = utils.generateId(10);
        this.issueDate = utils.getEpochTime();
        this.dueDate = utils.getDateAfterDays(issueDate, 5);
        this.returnDate = 0;
        this.member = member;
        this.book = book;
        this.fineAmount = fineAmount;
    }

    /**
     * Gets the issue ID
     * 
     * @return the issue ID
     */
    public long getIssueId() {
        return this.issueId;
    }

    /**
     * Gets the member who issued the book
     * 
     * @return the member who issued the book
     */
    public Member getMember() {
        return this.member;
    }

    /**
     * Gets the book that was issued
     * 
     * @return the book that was issued
     */
    public Book getBook() {
        return this.book;
    }

    /**
     * Gets the issue date
     * 
     * @return the issue date
     */
    public long getIssueDate() {
        return this.issueDate;
    }

    /**
     * Gets the due date
     * 
     * @return the due date
     */
    public long getDueDate() {
        return this.dueDate;
    }

    /**
     * Gets the return date
     * 
     * @return the return date
     */
    public long getReturnDate() {
        return this.returnDate;
    }

    /**
     * Gets the fine amount
     * 
     * @return the fine amount
     */
    public double getFineAmount() {
        return this.fineAmount;
    }

    /**
     * Sets the issue ID
     * 
     * @param issueId the issue ID to set
     */
    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    /**
     * Sets the member who issued the book
     * 
     * @param member the member who issued the book to set
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Sets the book that was issued
     * 
     * @param book the book that was issued to set
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Sets the issue date
     * 
     * @param issueDate the issue date to set
     */
    public void setIssueDate(long issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * Sets the due date
     * 
     * @param dueDate the due date to set
     */
    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Sets the return date
     * 
     * @param returnDate the return date to set
     */
    public void setReturnDate(long returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Sets the fine amount
     * 
     * @param fineAmount the fine amount to set
     */
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    @Override
    public String toString() {
        return "BookIssue [issueId=" + issueId + ", memberId=" + member.getMemberId() + ", bookId=" + book.getISBN()
                + ", issueDate=" + issueDate + ", dueDate=" + dueDate + ", returnDate=" + returnDate
                + ", fineAmount=" + fineAmount + "]";
    }
}
