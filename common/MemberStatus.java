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

    public static MemberStatus getStatusByName(String name) throws Exception {
        for (MemberStatus status : MemberStatus.values()) {
            if (status.name().equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new Exception("Invalid member status");
    }
}
