package common;

/**
 * Represents the possible statuses of a library member.
 * This enum defines the various states a member can be in during their
 * membership lifecycle.
 */
public enum MemberStatus {
    /** Member has an active membership with full borrowing privileges */
    ACTIVE,

    /** Member's privileges are temporarily suspended due to policy violations */
    SUSPENDED,

    /** Member's membership has expired and needs renewal */
    EXPIRED;

    /**
     * Retrieves a MemberStatus enum value based on its 1-based index.
     * 
     * @param number the status number (1=ACTIVE, 2=SUSPENDED, 3=EXPIRED)
     * @return the corresponding MemberStatus enum value
     * @throws Exception if the provided number doesn't correspond to any status
     */
    public static MemberStatus getStatus(int number) throws Exception {
        for (MemberStatus status : MemberStatus.values()) {
            if (status.ordinal() == number - 1) {
                return status;
            }
        }
        throw new Exception("Invalid member status");
    }

    public static MemberStatus getStatusByName(String name) throws Exception {
        for (MemberStatus status : MemberStatus.values()) {
            if (status.name().equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new Exception("Invalid member status name: " + name);
    }
}
