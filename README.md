# Library Management System

A comprehensive Java-based Library Management System that provides complete functionality for managing books, members, book issues, and reservations. The system supports multiple book types (Physical, E-books, Audio books) and different member categories (Students, Faculty, Regular members) with role-based borrowing policies and automated fine calculation.

## Key Features

### 📚 **Multi-Format Book Management**
- **Physical Books**: Track copies, availability, and reservations with inventory management
- **E-Books**: Manage digital formats with DRM protection status and unlimited availability
- **Audio Books**: Handle narrator information, audio formats, and duration tracking
- **Categories**: Fiction, Non-Fiction, Science, Technology, History, Biography, Self-Help, Children, Poetry, Drama

### 👥 **Role-Based Member System**
- **Student Members**: 3 books limit, ₹2/day fine, 3-day grace period, 2 renewals, ₹100 max fine
- **Faculty Members**: 5 books limit, ₹1/day fine, 5-day grace period, 3 renewals, ₹50 max fine
- **Regular Members**: 2 books limit, ₹3/day fine, 2-day grace period, 1 renewal, ₹200 max fine

### 🔄 **Advanced Borrowing System**
- Automated due date calculation (5-day default period)
- Grace period implementation based on member type
- Overdue fine calculation with daily rates
- Book renewal with policy validation
- Reservation queue management (FIFO)
- Duplicate reservation prevention

### 📊 **Analytics & Reporting**
- Popular books analysis with issue/reservation counts
- Monthly borrowing trend reports
- Fine collection summaries
- Member engagement analytics
- Overdue book tracking
- Book recommendation engine based on popular authors

### 💾 **Data Management**
- Dual-format persistence (text + serialized binary)
- Data import/export functionality
- Automatic backup creation
- Data integrity validation
- Cross-platform file handling

## Project Running Steps

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Any Java IDE (Eclipse, IntelliJ IDEA, VS Code) or command line

### Setup Instructions

1. **Clone or Download the Project**
   ```bash
   git clone <repository-url>
   cd library-management-system
   ```

2. **Create Required Directories**
   ```bash
   mkdir -p db import
   ```

3. **Compile the Project**
   ```bash
   javac -d . main/*.java model/*.java service/*.java common/*.java
   ```

4. **Run the Application**
   ```bash
   java main.LibraryManagementSystem
   ```

5. **Using an IDE**
   - Import the project into your preferred IDE
   - Set the main class as `main.LibraryManagementSystem`
   - Run the project

### Initial Setup
- The system will create database files automatically in the `db/` directory
- Sample data can be imported using the import functionality (Menu option 25)
- The system supports both text and serialized data formats for backup

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
├── import/                 # Directory for data import files
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
    ├── ImportExportService.java # Data import/export functionality with CSV parsing and file handling
    ├── MemberService.java       # Member registration, updates, search by ID/name/email, and account management
    └── ReservationService.java  # Book reservation queue management with conflict resolution and FIFO ordering
```

### File Descriptions

#### Common Package
- **BookCategory.java**: Enumeration defining all supported book categories with validation methods
- **DataManager.java**: Centralized data persistence manager handling object serialization and file I/O operations
- **MemberPolicy.java**: Policy engine defining borrowing rules, fine rates, and limits for different member types
- **MemberStatus.java**: Member status enumeration with validation for active, suspended, and expired states
- **utils.java**: Comprehensive utility class with ID generation, date/time handling, file operations, and backup functionality

#### Model Package
- **Book.java**: Abstract base class defining common book properties and abstract availability method
- **PhysicalBook.java**: Concrete implementation for physical books with copy management and availability tracking
- **EBook.java**: Digital book implementation with file format and DRM protection attributes
- **AudioBook.java**: Audio book implementation with narrator, format, and duration information
- **Member.java**: Abstract member class with borrowing logic, fine calculation, and policy enforcement
- **StudentMember.java**: Student-specific member with academic information and moderate borrowing privileges
- **FacultyMember.java**: Faculty member with enhanced privileges and extended borrowing capabilities
- **RegularMember.java**: General public member with basic borrowing rights and standard policies
- **BookIssue.java**: Transaction record for book borrowing with dates, fines, and overdue calculation
- **Reservation.java**: Reservation record maintaining queue order and member-book associations

#### Service Package
- **BookService.java**: Complete book management including CRUD operations, search functionality, and availability checking
- **MemberService.java**: Member lifecycle management with registration, updates, search, and account maintenance
- **BookIssueService.java**: Core borrowing operations including issue, return, renewal, and overdue management
- **ReservationService.java**: Reservation queue management with FIFO ordering and conflict resolution
- **ImportExportService.java**: Data migration utilities for importing/exporting books, members, and transaction data

## Menu Items and Functionality

### 🔹 **Book Operations (1-5)**

**1. Add New Book**
- Supports adding Physical Books, E-Books, and Audio Books
- Validates all required fields (title, author, publisher, publication year, category)
- For Physical Books: Collects page count and total copies information
- For E-Books: Captures file format and DRM protection status
- For Audio Books: Records narrator name, audio format, and duration
- Automatically generates unique ISBN numbers
- Saves to both text and serialized database formats

**2. Update Book Info**
- Allows modification of existing book details by ISBN lookup
- Supports updating title, author, publisher, and publication year
- For Physical Books: Enables adding more copies (increases both total and available counts)
- Validates all input fields and prevents invalid data entry
- Maintains data integrity across all book formats

**3. Search Books**
- **By ISBN**: Exact match search for specific book identification
- **By Title**: Partial match search (case-insensitive) for flexible book discovery
- **By Author**: Partial match search to find all books by specific authors
- Displays results in formatted table with all relevant book information
- Shows availability status and copy information for physical books

**4. View Book Details**
- Displays comprehensive information for a specific book by ISBN
- Shows all book attributes including category, publication details
- For Physical Books: Displays total, available, and reserved copy counts
- For E-Books: Shows file format and DRM protection status
- For Audio Books: Displays narrator, format, and duration information

**5. Check Availability**
- Verifies if a specific book is available for borrowing
- For Physical Books: Checks if available copies > 0
- For Digital Books: Always shows as available (unlimited copies)
- Provides clear availability status messages

### 👥 **Member Operations (6-10)**

**6. Register Member**
- Supports registration of Students, Faculty, and Regular members
- Validates all required information (name, email, phone number)
- For Students: Collects student ID, academic year, and department
- For Faculty: Records faculty ID, department, and designation
- For Regular Members: Basic information with standard privileges
- Automatically generates unique member IDs and sets initial status to ACTIVE

**7. Update Member Info**
- Allows modification of member details by member ID lookup
- Supports updating name, email, phone number, and membership status
- Validates input data and prevents invalid entries
- Maintains member type-specific information integrity
- Updates both in-memory and persistent storage

**8. Search Members**
- **By Member ID**: Exact match search for specific member identification
- **By Name**: Partial match search (case-insensitive) for flexible member discovery
- **By Email**: Partial match search for email-based member lookup
- Displays results in formatted table with member details and borrowing status
- Shows current borrowed books, fine amounts, and membership status

**9. View Member History**
- Intended to show complete borrowing history for a specific member
- Would include past issues, returns, renewals, and fine payments

**10. Fine Management**
- *Currently not implemented* - Placeholder for future functionality
- Intended to handle fine payments and adjustments
- Would provide fine calculation details and payment processing

### 📖 **Borrowing Operations (11-15)**

**11. Issue Book**
- Comprehensive book lending process with multiple validations
- Verifies member eligibility (active status, within borrowing limits, acceptable fine levels)
- Checks book availability and handles reservation conflicts
- For reserved books: Only allows the reserving member to borrow
- Automatically removes fulfilled reservations from the queue
- Updates book availability (decreases available copies for physical books)
- Records issue transaction with automatic due date calculation (5 days default)
- Updates member's borrowed book count and borrowing status

**12. Return Book**
- Processes book returns with comprehensive validation
- Verifies the book was actually issued to the returning member
- Calculates overdue fines based on member type and days overdue
- Applies grace periods according to member policies
- Updates book availability (increases available copies for physical books)
- Records return date and final fine amount in transaction history
- Updates member's borrowed book count and fine balance

**13. Renew Book**
- Extends borrowing period for eligible members and books
- Validates member renewal eligibility (active status, no excessive fines, within renewal limits)
- Checks for conflicting reservations by other members
- Prevents renewal if book is reserved by someone else
- Extends due date by default period (5 days)
- Updates member's renewal count and borrowing status
- Handles reservation conflicts for same-member reservations

**14. Reserve Book**
- Allows members to reserve books that are currently unavailable
- Validates member eligibility using same criteria as book issuing
- Prevents duplicate reservations by the same member for the same book
- For Physical Books: Updates reserved and available copy counts
- For Digital Books: Records reservation for tracking purposes
- Maintains FIFO (First-In-First-Out) reservation queue
- Integrates with issuing process for automatic reservation fulfillment

**15. View Overdue Books**
- Displays all books that are past their due date and not yet returned
- Shows book details, borrower information, and overdue duration
- Includes member type and contact information for follow-up
- Formatted table display with issue date and due date information
- Helps library staff identify books requiring immediate attention

### 🚀 **Advanced Features (16-19)**

**16. Book Recommendations**
- Analyzes borrowing patterns of a specific member to suggest popular books
- Identifies top 10 most frequently borrowed authors
- Recommends other available books by these popular authors
- Uses machine learning-like approach based on historical data
- Displays recommended books in formatted table with availability status

**17. Advanced Search**
- **Multiple Author Search**: Search books by multiple authors (comma-separated)
- **Category and Year Filter**: Filter available books by category and publication year range
- **Members with Overdue Books**: List all members currently having overdue books
- Provides sophisticated search capabilities beyond basic book/member searches

**18. Popular Books Report**
- Identifies and displays the top 5 most borrowed books
- Shows issue count and current reservation count for each popular book
- Analyzes borrowing frequency to determine popularity rankings
- Helps in inventory management and acquisition decisions
- Formatted display with statistical information

**19. Member Analytics**
- *Currently not implemented* - Placeholder for future functionality
- Intended to provide detailed member behavior analysis
- Would include borrowing patterns, fine history, and engagement metrics

### 📊 **Reports & Analytics (20-23)**

**20. Borrowing Reports**
- Generates monthly borrowing trend analysis
- Groups book issues by month and year for pattern identification
- Displays borrowing volume over time periods
- Helps in understanding seasonal borrowing patterns
- Useful for resource planning and staff scheduling

**21. Fine Collection Reports**
- Calculates total fines collected from all book issues
- Provides financial summary of penalty revenue
- Includes fines from overdue books and policy violations
- Helps in financial reporting and budget planning

**22. Book Popularity Analysis**
- Detailed analysis of book borrowing patterns
- Same functionality as Popular Books Report (Menu 18)
- Identifies trending books and authors
- Supports collection development decisions

**23. Member Engagement Reports**
- *Currently not implemented* - Placeholder for future functionality
- Intended to analyze member activity and engagement levels
- Would include metrics like borrowing frequency, renewal patterns, and fine history

### ⚙️ **System Operations (24-26)**

**24. Data Backup**
- *Currently not implemented* - Placeholder for future functionality
- Intended to create comprehensive system backups
- Would include all database files and configuration data

**25. Data Import/Export**
- **Import Books**: Load book data from CSV files in the import/ directory
- **Import Members**: Load member data from formatted text files
- **Import Book Issues**: Load transaction history from external files
- Supports data migration and bulk data entry
- Validates imported data and maintains referential integrity
- Handles different book types and member categories during import

**26. System Configuration**
- *Currently not implemented* - Placeholder for future functionality
- Intended for system settings and policy configuration
- Would allow customization of borrowing policies and fine rates

### 🚪 **Exit Operation (27)**

**27. Exit**
- Gracefully terminates the application
- Displays farewell message
- Ensures all data is properly saved before exit
- Closes all system resources and database connections

## Technical Implementation Details

### Data Persistence
- **Dual Format Storage**: Each data type is saved in both human-readable text format (.txt) and efficient binary format (.ser)
- **Automatic Backup**: Text files serve as backup and manual inspection capability
- **Serialization**: Binary files enable fast loading and object integrity

### Policy Engine
- **Configurable Rules**: All borrowing policies are centralized in MemberPolicy class
- **Type-Based Logic**: Different rules for Students, Faculty, and Regular members
- **Extensible Design**: Easy to add new member types or modify existing policies

### Error Handling
- **Comprehensive Validation**: Input validation at multiple levels
- **Graceful Degradation**: System continues operation even with data inconsistencies
- **User-Friendly Messages**: Clear error messages for all failure scenarios

### Performance Optimization
- **Efficient Search**: Stream API usage for fast data filtering and searching
- **Memory Management**: Lazy loading and efficient data structures
- **Scalable Design**: Architecture supports large datasets and concurrent operations

This Library Management System provides a complete solution for modern library operations with robust data management, flexible policies, and comprehensive reporting capabilities.