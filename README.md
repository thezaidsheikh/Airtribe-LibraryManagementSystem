# Project

A comprehensive Library Management System built in Java that provides complete functionality for managing books, members, book issues, and reservations. The system supports multiple book types (Physical, E-books, Audio books) and different member categories (Students, Faculty, Regular members) with role-based borrowing policies.

## Project Structure

```
├── common/                 # Common utilities and enums
│   ├── BookCategory.java   # Enum for book categories (Fiction, Non_Fiction, Science, Technology, History, Biography, Self_Help, Children, Poetry, Drama)
│   ├── DataManager.java    # Handles serialization/deserialization of data files with ObjectInputStream/ObjectOutputStream
│   ├── MemberPolicy.java   # Defines borrowing limits, daily fines, grace periods, renewal limits, and max fines by member type
│   ├── MemberStatus.java   # Enum for member status (ACTIVE, SUSPENDED, EXPIRED)
│   └── utils.java          # Utility functions for ID generation, epoch time handling, date conversion, file operations, and zip backup
├── db/                     # Database files (serialized and text format)
│   ├── bookIssues.ser/.txt # Book issue transaction records with member and book references
│   ├── books.ser/.txt      # Complete book inventory with all book types and their specific attributes
│   ├── members.ser/.txt    # Member records with type-specific details (student ID, faculty ID, department, etc.)
│   └── reservations.ser/.txt # Book reservation queue records with member and book IDs
├── main/                   # Application entry point and menu system
│   ├── LibraryManagementSystem.java # Main class with application entry point
│   └── LibraryManagementMenu.java   # Interactive console menu system with 27 operations organized in categories
├── model/                  # Data models and entities
│   ├── AudioBook.java      # Audio book with narrator name, audio format, and length in hours
│   ├── Book.java           # Abstract base class for all book types with ISBN, title, author, publisher, year, category
│   ├── BookIssue.java      # Book borrowing transaction with issue/due/return dates, fine calculation, and overdue tracking
│   ├── EBook.java          # Electronic book with file format and DRM protection status
│   ├── FacultyMember.java  # Faculty member with faculty ID, department, designation, and enhanced privileges
│   ├── Member.java         # Abstract base class for all member types with borrowing logic, fine management, and policy enforcement
│   ├── PhysicalBook.java   # Physical book with pages, total/available/reserved copies management
│   ├── RegularMember.java  # General public member with basic borrowing privileges
│   ├── Reservation.java    # Book reservation record with member ID, book ID, and reservation date
│   └── StudentMember.java  # Student member with student ID, academic year, department, and moderate privileges
└── service/                # Business logic and operations
    ├── BookIssueService.java    # Handles book borrowing, returning, renewal, overdue tracking, and fine calculation
    ├── BookService.java         # Book management (add, update, search by ISBN/title/author, availability checking)
    ├── MemberService.java       # Member registration, updates, search by ID/name/email, and account management
    └── ReservationService.java  # Book reservation queue management with conflict resolution
```

## Running Guide

### Prerequisites
- Java 8 or higher
- Command line access

### Compilation and Execution
```bash
# Compile all Java files
javac -d . main/*.java model/*.java service/*.java common/*.java

# Run the application
java main.LibraryManagementSystem
```

### Data Storage
- The system automatically creates a `db/` directory for data persistence
- Data is stored in both serialized (.ser) and text (.txt) formats for performance and readability
- All data is loaded automatically on startup and saved after each operation

## Library Menu Operations

The system provides a comprehensive menu with 27 operations organized into functional categories:

### Book Operations (1-5)
- **Add New Book**: Create physical books (with pages/copies), e-books (with format/DRM), or audiobooks (with narrator/duration)
- **Update Book Info**: Modify book details and manage physical copy inventory
- **Search Books**: Find books by ISBN, title, or author with partial matching support
- **View Book Details**: Display complete information including type-specific attributes
- **Check Availability**: Verify if physical books have available copies for borrowing

### Member Operations (6-10)
- **Register Member**: Add students (with student ID/academic year/department), faculty (with faculty ID/department/designation), or regular members
- **Update Member Info**: Modify member details and change membership status (Active/Suspended/Expired)
- **Search Members**: Find members by ID, name, or email with partial matching
- **View Member History**: Display borrowing history and current account status (not implemented)
- **Fine Management**: Handle fine payments and member account management (not implemented)

### Borrowing Operations (11-15)
- **Issue Book**: Lend books to members with automatic due date calculation, policy validation, and reservation checking
- **Return Book**: Process book returns with overdue fine calculation and copy management
- **Renew Book**: Extend borrowing period subject to renewal limits and reservation conflicts
- **Reserve Book**: Allow members to reserve books with queue management and copy tracking
- **View Overdue Books**: Display books past their due date with member details and borrowing information

### Advanced Features (16-19)
- **Book Recommendations**: Suggest books based on member preferences (not implemented)
- **Advanced Search**: Complex search with multiple criteria (not implemented)
- **Popular Books Report**: Analytics on most borrowed books (not implemented)
- **Member Analytics**: Insights into member borrowing patterns (not implemented)

### Reports & Analytics (20-23)
- **Borrowing Reports**: Comprehensive borrowing statistics (not implemented)
- **Fine Collection Reports**: Financial reporting on fines collected (not implemented)
- **Book Popularity Analysis**: Detailed circulation analysis (not implemented)
- **Member Engagement Reports**: Member activity metrics (not implemented)

### System Operations (24-26)
- **Data Backup**: Create backup copies of all system data (not implemented)
- **Data Import/Export**: Transfer data to/from external systems (not implemented)
- **System Configuration**: Modify system settings and policies (not implemented)

### Exit (27)
- **Exit**: Safely terminate the application

## Key Features

**Multi-Book Type Support**: 
- Physical books with copy management and availability tracking
- E-books with file format and DRM protection details
- Audiobooks with narrator information and duration tracking

**Role-Based Member Policies**:
- Students: 3 books limit, ₹2/day fine, 3-day grace period, 2 renewals, ₹100 max fine
- Faculty: 5 books limit, ₹1/day fine, 5-day grace period, 3 renewals, ₹50 max fine  
- Regular: 2 books limit, ₹3/day fine, 2-day grace period, 1 renewal, ₹200 max fine

**Reservation System**: Members can reserve books with automatic queue processing and conflict resolution during issue/renewal

**Fine Management**: Automatic calculation of overdue fines with grace periods, daily rates, maximum limits, and account suspension when limits exceeded

**Data Persistence**: Dual storage format using Java serialization for performance and text files for human readability, with automatic backup capabilities

**Comprehensive Search**: Multiple search criteria with partial matching, case-insensitive operations, and detailed result display

**Business Rule Enforcement**: Borrowing limits, renewal restrictions, reservation priorities, fine-based suspensions, and member status validation ensure fair library operations