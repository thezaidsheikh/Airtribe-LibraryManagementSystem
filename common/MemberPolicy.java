package common;

public class MemberPolicy {
    public static int getBorrowingLimit(String type) {
        switch (type) {
            case "Student":
                return 5;
            case "Faculty":
                return 10;
            case "Regular":
                return 2;
            default:
                return 0;
        }
    }

    public static double getDailyFine(String type) {
        switch (type) {
            case "Student":
                return 2.0; // Rs. 2 per day
            case "Faculty":
                return 1.0; // Rs. 1 per day
            case "Regular":
                return 3.0; // Rs. 3 per day
            default:
                return 2.0;
        }
    }

    public static int getGracePeriod(String type) {
        switch (type) {
            case "Student":
                return 5;
            case "Faculty":
                return 10;
            case "Regular":
                return 2;
            default:
                return 0;
        }
    }

    public static double getMaxFine(String type) {
        switch (type) {
            case "Student":
                return 100.0;
            case "Faculty":
                return 50.0;
            case "Regular":
                return 200.0;
            default:
                return 100.0;
        }
    }
}
