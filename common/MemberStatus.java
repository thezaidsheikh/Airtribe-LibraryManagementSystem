package common;

public enum MemberStatus {
    ACTIVE,
    SUSPENDED,
    EXPIRED;

    public static MemberStatus getStatus(int number) throws Exception {
        for (MemberStatus status : MemberStatus.values()) {
            if (status.ordinal() == number - 1) {
                return status;
            }
        }
        throw new Exception("Invalid member status");
    }
}
