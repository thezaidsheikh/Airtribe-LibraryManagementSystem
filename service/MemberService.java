package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import common.MemberStatus;
import common.utils;
import model.FacultyMember;
import model.Member;
import model.RegularMember;
import model.StudentMember;

public class MemberService {
    Scanner scn = new Scanner(System.in);
    List<Member> members = new ArrayList<>();

    /**
     * Loads members from a file
     */
    public void loadMembers() {
        this.members = utils.loadData("./db/members.ser");
    }

    /**
     * Helper method to display a list of members.
     */
    private void showMemberList(List<Member> membersList) {
        System.out.println("Result -\n");
        if (membersList.size() == 0) {
            System.out.println("Error: No members found");
            System.out.println("=====================================");
            return;
        }

        String[] headers = { "MEMBER ID", "NAME", "EMAIL", "PHONE", "MEMBERSHIP DATE", "MEMBER TYPE",
                "BORROWING LIMIT",
                "CURRENT BORROWED BOOKS", "TOTAL FINE AMOUNT", "MEMBERSHIP STATUS", "STUDENT ID", "ACADEMIC YEAR",
                "DEPARTMENT", "FACULTY ID", "DESIGNATION" };

        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (Member member : membersList) {

            colWidths[0] = Math.max(colWidths[0], String.valueOf(member.getMemberId()).length());
            colWidths[1] = Math.max(colWidths[1], member.getName().length());
            colWidths[2] = Math.max(colWidths[2], member.getEmail().length());
            colWidths[3] = Math.max(colWidths[3], String.valueOf(member.getPhone()).length());
            colWidths[4] = Math.max(colWidths[4], String.valueOf(member.getMembershipDate()).length());
            colWidths[5] = Math.max(colWidths[5], member.getMemberType().length());
            colWidths[6] = Math.max(colWidths[6], String.valueOf(member.getBorrowingLimit()).length());
            colWidths[7] = Math.max(colWidths[7], String.valueOf(member.getCurrentBorrowedBooks()).length());
            colWidths[8] = Math.max(colWidths[8], String.valueOf(member.getTotalFineAmount()).length());
            colWidths[9] = Math.max(colWidths[9], member.getMembershipStatus().name().length());
            if (member instanceof StudentMember) {
                StudentMember studentMember = (StudentMember) member;
                colWidths[10] = Math.max(colWidths[10], String.valueOf(studentMember.getStudentId()).length());
                colWidths[11] = Math.max(colWidths[11], String.valueOf(studentMember.getAcademicYear()).length());
                colWidths[12] = Math.max(colWidths[12], String.valueOf(studentMember.getDepartment()).length());
            } else if (member instanceof FacultyMember) {
                FacultyMember facultyMember = (FacultyMember) member;
                colWidths[12] = Math.max(colWidths[12], String.valueOf(facultyMember.getDepartment()).length());
                colWidths[13] = Math.max(colWidths[13], String.valueOf(facultyMember.getFacultyId()).length());
                colWidths[14] = Math.max(colWidths[14], String.valueOf(facultyMember.getDesignation()).length());
            }
        }

        // Build format string
        String format = String.format(
                "%%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds %%-%ds %%-%ds%n",
                colWidths[0], colWidths[1], colWidths[2], colWidths[3], colWidths[4], colWidths[5], colWidths[6],
                colWidths[7], colWidths[8], colWidths[9], colWidths[10], colWidths[11], colWidths[12], colWidths[13],
                colWidths[14]);

        // Print header
        System.out.printf(format, (Object[]) headers);
        System.out.println();

        // Print rows
        for (Member member : membersList) {
            if (member instanceof StudentMember) {
                StudentMember studentMember = (StudentMember) member;
                System.out.printf(format,
                        String.valueOf(studentMember.getMemberId()),
                        studentMember.getName(),
                        studentMember.getEmail(),
                        String.valueOf(studentMember.getPhone()),
                        String.valueOf(utils.convertEpochToDate(studentMember.getMembershipDate())),
                        studentMember.getMemberType(),
                        String.valueOf(studentMember.getBorrowingLimit()),
                        String.valueOf(studentMember.getCurrentBorrowedBooks()),
                        String.valueOf(studentMember.getTotalFineAmount()),
                        studentMember.getMembershipStatus().name(),
                        String.valueOf(studentMember.getStudentId()),
                        String.valueOf(studentMember.getAcademicYear()),
                        studentMember.getDepartment(),
                        "-", "-");
            } else if (member instanceof FacultyMember) {
                FacultyMember facultyMember = (FacultyMember) member;
                System.out.printf(format,
                        String.valueOf(facultyMember.getMemberId()),
                        facultyMember.getName(),
                        facultyMember.getEmail(),
                        String.valueOf(facultyMember.getPhone()),
                        String.valueOf(utils.convertEpochToDate(facultyMember.getMembershipDate())),
                        facultyMember.getMemberType(),
                        String.valueOf(facultyMember.getBorrowingLimit()),
                        String.valueOf(facultyMember.getCurrentBorrowedBooks()),
                        String.valueOf(facultyMember.getTotalFineAmount()),
                        facultyMember.getMembershipStatus().name(),
                        "-", "-", facultyMember.getDepartment(), facultyMember.getFacultyId(),
                        facultyMember.getDesignation());
            } else {
                System.out.printf(format,
                        String.valueOf(member.getMemberId()),
                        member.getName(),
                        member.getEmail(),
                        String.valueOf(member.getPhone()),
                        String.valueOf(utils.convertEpochToDate(member.getMembershipDate())),
                        member.getMemberType(),
                        String.valueOf(member.getBorrowingLimit()),
                        String.valueOf(member.getCurrentBorrowedBooks()),
                        String.valueOf(member.getTotalFineAmount()),
                        member.getMembershipStatus().name(),
                        "-", "-", "-", "-", "-");
            }
        }
        System.out.println("\n===================================== END MEMBER LIST =============================\n");
    }

    /**
     * Registers a new member in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void registerMember() throws Exception {
        System.out.print("Enter member name (mandatory - max 15 characters): ");
        String name = scn.nextLine().trim();
        if (name.isEmpty() || name.length() > 15) {
            System.out.println("Name is invalid.");
            return;
        }

        System.out.print("Enter member email (mandatory - max 30 characters): ");
        String email = scn.nextLine().trim();
        if (email.isEmpty() || email.length() > 30) {
            System.out.println("Email is invalid.");
            return;
        }

        System.out.print("Enter member phone number (mandatory): ");
        long phone = Long.parseLong(scn.nextLine().trim());
        if (phone < 0 || String.valueOf(phone).length() > 10) {
            System.out.println("Phone number is invalid.");
            return;
        }

        System.out.println("Choose the member type: \n1. Student\n2. Faculty\n3. General Public");
        int memberType = Integer.parseInt(scn.nextLine().trim());
        if (memberType < 1 || memberType > 3) {
            System.out.println("Member type is invalid.");
            return;
        }

        if (memberType == 1) {
            System.out.print("Enter student ID (mandatory - max 15 characters): ");
            String studentId = scn.nextLine().trim();
            if (studentId.isEmpty() || studentId.length() > 15) {
                System.out.println("Student ID is invalid.");
                return;
            }

            System.out.print("Enter academic year (mandatory - max 15 characters): ");
            String academicYear = scn.nextLine().trim();
            if (academicYear.isEmpty() || academicYear.length() > 15) {
                System.out.println("Academic year is invalid.");
                return;
            }

            System.out.print("Enter department (mandatory - max 30 characters): ");
            String department = scn.nextLine().trim();
            if (department.isEmpty() || department.length() > 30) {
                System.out.println("Department is invalid.");
                return;
            }

            StudentMember studentMember = new StudentMember(name, email, phone, studentId, academicYear, department);
            this.members.add(studentMember);
        }

        if (memberType == 2) {
            System.out.print("Enter faculty ID (mandatory - max 15 characters): ");
            String facultyId = scn.nextLine().trim();
            if (facultyId.isEmpty() || facultyId.length() > 15) {
                System.out.println("Faculty ID is invalid.");
                return;
            }

            System.out.print("Enter department (mandatory - max 30 characters): ");
            String department = scn.nextLine().trim();
            if (department.isEmpty() || department.length() > 30) {
                System.out.println("Department is invalid.");
                return;
            }

            System.out.print("Enter designation (mandatory - max 30 characters): ");
            String designation = scn.nextLine().trim();
            if (designation.isEmpty() || designation.length() > 30) {
                System.out.println("Designation is invalid.");
                return;
            }

            FacultyMember facultyMember = new FacultyMember(name, email, phone, facultyId, department, designation);
            this.members.add(facultyMember);
        }

        if (memberType == 3) {
            RegularMember regularMember = new RegularMember(name, email, phone);
            this.members.add(regularMember);
        }

        this.updateMemberInDatabase();

    }

    /**
     * Updates a member's information in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void updateMemberInfo() throws Exception {
        System.out.print("Enter member ID: ");
        long memberId = Long.parseLong(scn.nextLine());
        if (memberId <= 0) {
            System.out.println("Member ID is invalid");
            return;
        }

        // Find the member by ID
        int memberIndex = IntStream.range(0, this.members.size())
                .filter(i -> this.members.get(i).getMemberId() == memberId)
                .findFirst().orElse(-1);
        if (memberIndex == -1) {
            System.out.println("Member not found");
            return;
        }

        // Get the member details
        Member member = this.members.get(memberIndex);
        System.out.print(
                "Enter the member name.(current: " + member.getName()
                        + ") (press enter to skip - max 15 characters): ");
        String name = scn.nextLine();
        if (!name.isEmpty() && (name.length() > 15)) {
            System.out.println("Member name is invalid");
            return;
        }
        if (!name.isEmpty()) {
            member.setName(name);
        }

        System.out.print("Enter the member email.(current: " + member.getEmail()
                + ") (press enter to skip - max 30 characters): ");
        String email = scn.nextLine();
        if (!email.isEmpty() && (email.length() > 30)) {
            System.out.println("Member email is invalid");
            return;
        }
        if (!email.isEmpty()) {
            member.setEmail(email);
        }

        System.out.print("Enter the member phone number.(current: " + member.getPhone()
                + ") (press enter to skip - max 10 characters): ");
        String phone = scn.nextLine();
        if (!phone.isEmpty() && (phone.length() > 10)) {
            System.out.println("Member phone number is invalid");
            return;
        }
        if (!phone.isEmpty()) {
            member.setPhone(Long.parseLong(phone));
        }

        System.out.print("Choose the membership status: \n1. Active\n2. Suspended\n3. Expired\n");
        int memberStatus = Integer.parseInt(scn.nextLine());
        if (memberStatus < 1 || memberStatus > 3) {
            System.out.println("Member status is invalid");
            return;
        }
        member.setMembershipStatus(MemberStatus.getStatus(memberStatus));
        this.members.set(memberIndex, member);

        this.updateMemberInDatabase();
    }

    /**
     * Searches a member in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void searchMember() throws Exception {
        System.out.println("1.Search Member by Member ID");
        System.out.println("2.Search Member by Name");
        System.out.println("3.Search Member by Email");
        String value = scn.nextLine().split("\\s+")[0];
        switch (value) {
            case "1":
                searchMemberByMemberId();
                break;
            case "2":
                searchMemberByName();
                break;
            case "3":
                searchMemberByEmail();
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    /**
     * Searches a member by member ID in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void searchMemberByMemberId() throws Exception {
        System.out.print("Enter member ID: ");
        long memberId = Long.parseLong(scn.nextLine());
        if (memberId < 0) {
            throw new Exception("Invalid member ID");
        }

        ArrayList<Member> member = new ArrayList<Member>();
        for (Member m : this.members) {
            if (m.getMemberId() == memberId) {
                member.add(m);
                break;
            }
        }
        if (member.isEmpty()) {
            throw new Exception("Member not found");
        }
        showMemberList(member);
    }

    /**
     * Searches a member by name in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void searchMemberByName() throws Exception {
        System.out.print("Enter member name: ");
        String name = scn.nextLine();
        if (name.isEmpty()) {
            throw new Exception("Invalid member name");
        }

        ArrayList<Member> member = new ArrayList<Member>();
        for (Member m : this.members) {
            if (m.getName().toLowerCase().contains(name.toLowerCase())) {
                member.add(m);
            }
        }
        if (member.isEmpty()) {
            throw new Exception("Member not found");
        }
        showMemberList(member);
    }

    /**
     * Searches a member by email in the library management system.
     * 
     * @throws Exception if any error occurs
     */
    public void searchMemberByEmail() throws Exception {
        System.out.print("Enter member email: ");
        String email = scn.nextLine();
        if (email.isEmpty()) {
            throw new Exception("Invalid member email");
        }

        ArrayList<Member> member = new ArrayList<Member>();
        for (Member m : this.members) {
            if (m.getEmail().toLowerCase().contains(email.toLowerCase())) {
                member.add(m);
            }
        }
        if (member.isEmpty()) {
            throw new Exception("Member not found");
        }
        showMemberList(member);
    }

    /**
     * Retrieves a member by their unique member ID.
     *
     * @param memberId the unique identifier of the member to retrieve
     * @return the Member object if found, null otherwise
     */
    protected Member getMemberById(long memberId) {
        for (Member m : this.members) {
            if (m.getMemberId() == memberId) {
                return m;
            }
        }
        return null;
    }

    /**
     * Updates a member's information in the in-memory list of members.
     *
     * @param member the Member object with updated information
     * @throws Exception if the member is not found in the list
     */
    protected void updateMemberInList(Member member) throws Exception {
        int memberIndex = IntStream.range(0, this.members.size())
                .filter(i -> this.members.get(i).getMemberId() == member.getMemberId())
                .findFirst().orElse(-1);
        if (memberIndex == -1) {
            throw new Exception("Member not found");
        }
        this.members.set(memberIndex, member);
    }

    /**
     * Saves the current list of members to the database file.
     * The members are serialized and saved to './db/members.txt'.
     *
     * @throws Exception if there is an error during the save operation
     */
    protected void updateMemberInDatabase() throws Exception {
        utils.saveData("./db/members.txt", this.members);
    }

    /**
     * Replaces the list of members with the given list
     * 
     * @param members the new list of members
     */
    protected void replaceMemberList(List<Member> members) {
        this.members = members;
    }
}
