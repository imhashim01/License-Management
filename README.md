# License-Management
A powerful, intuitive, and fully-featured License Management System built using Java, AWT, and Swing, designed to simplify the process of managing software licenses for individuals, developers, or enterprises.
Project Structure and Packages:
The project is organized into three main packages:
•	Manager: Contains the core business logic and data management classes. These classes interact directly with the database via MongoCollection objects and encapsulate the operations related to users, licenses, authentication, and notifications.
•	Model: Defines the data structures  that represent the entities within the application, such as User, License, and Notification. These classes are simple data holders with getters and setters.
•	ui: Houses the graphical user interface (GUI) components built using Java Swing. Each class in this package represents a specific window or panel in the application, handling user interactions and displaying data.
•	Utils: Provides utility classes for common tasks, such as database operations (DbUtil) and system-specific functionalities like detecting installed applications (InstalledAppDetector).
Key Properties and Functionalities
•	User Authentication and Registration: Secure login and new user creation with password hashing.
•	License Management: Comprehensive CRUD operations for licenses, including details like software name, type, key, and expiration date.
•	User Management: CRUD operations for users, with the ability to assign licenses and view individual license profiles.
•	Notifications: System for generating and displaying notifications, particularly for expiring licenses.
•	Reporting:
o	License Usage Report: Shows which licenses are assigned to whom.
o	License Expiry Report: Lists licenses that are due to expire soon.
o	User License Report: Provides a detailed breakdown of licenses for each user.
o	Expiry Reminders: Functionality to trigger reminders for expiring licenses.
o	Installed Applications: Ability to detect and list applications installed on the local Windows machine.
•	Data Persistence: Uses MongoDB as the backend database for storing all application data.
•	Modular Design: The application is structured into Model, Manager, ui, and Utils packages, promoting separation of concerns and maintainability.
•	Swing GUI: A desktop application built with Java Swing, featuring a modern look and feel with styled buttons and tables.
•	Error Handling: Basic try-catch blocks are implemented for database operations and image loading.
•	Graceful Shutdown: The MongoDB client is closed gracefully when the application exits.

Technologies Used
•	Language: Java
•	GUI Framework: Java Swing
•	Database: MongoDB (with MongoDB Java Driver)
•	Build Tool: (Assumed, typically Maven or Gradle for dependency management)
•	Hashing: SHA-256 (for passwords, with a note on security improvement needed)
