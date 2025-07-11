package model;

import common.utils;

public class Reservation {
    private static final long serialVersionUID = 1L;

    private long bookId;
    private long memberId;
    private long reservationDate;

    public Reservation(long bookId, long memberId) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.reservationDate = utils.getEpochTime();
    }

    public long getBookId() {
        return this.bookId;
    }

    public long getMemberId() {
        return this.memberId;
    }

    public long getReservationDate() {
        return this.reservationDate;
    }

    @Override
    public String toString() {
        return "Reservation [bookId=" + bookId + ", memberId=" + memberId + ", reservationDate=" + reservationDate
                + "]";
    }
}
