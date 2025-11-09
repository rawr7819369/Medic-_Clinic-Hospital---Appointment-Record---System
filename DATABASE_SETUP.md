# Database Setup Guide for MediConnect+

## Overview
The MediConnect+ system now includes full database integration using MySQL. All patient registrations, doctor additions, and other data will be saved to the database.

## Prerequisites
- MySQL Server (XAMPP includes this)
- MySQL JDBC Connector (mysql-connector-j-9.5.0.jar - already included in the project)

## Database Setup Steps

### 1. Start MySQL Server
Make sure MySQL is running in XAMPP:
- Open XAMPP Control Panel
- Start the MySQL service

### 2. Create the Database
Run the SQL script to create the database and tables:

```bash
# In MySQL command line or phpMyAdmin
mysql -u root -p < create_database.sql
```

Or manually:
- Open phpMyAdmin (http://localhost/phpmyadmin)
- Import the `create_database.sql` file

### 3. Verify Database Configuration
The database connection is configured in `src/util/DatabaseConnection.java`:
- Database URL: `jdbc:mysql://localhost:3306/mediconnect_db`
- Username: `root`
- Password: `` (empty by default)

If your MySQL has a different password, update the `DB_PASSWORD` constant in `DatabaseConnection.java`.

## How It Works

### Database Integration
- When you register a new patient, the data is saved to both:
  1. In-memory storage (DataStore) for fast access
  2. MySQL database (persistent storage)

### Automatic Data Loading
When the application starts:
1. Loads default data (admin, sample doctors, patient)
2. Loads existing data from the database
3. Updates the in-memory storage with database data

### Data Persistence
- All new registrations are automatically saved to the database
- Appointments, medical records, and prescriptions are saved to the database
- Data persists across application restarts

## Testing the Database Integration

### 1. Test Database Connection
Run the test script:
```bash
java -cp "mysql-connector-j-9.5.0.jar;.;util;service;model" TestDatabase
```

### 2. Test Patient Registration
1. Start the application
2. Register a new patient
3. Check the database:
   ```sql
   SELECT * FROM patients;
   SELECT * FROM users;
   ```

### 3. Verify Data Persistence
1. Register a patient
2. Close the application
3. Restart the application
4. Verify the patient still exists in the system

## Troubleshooting

### Connection Error
If you get "Connection refused" error:
- Make sure MySQL is running
- Check if the port is correct (default: 3306)
- Verify the database exists

### Driver Not Found
Make sure the MySQL connector JAR is in the classpath:
```bash
java -cp "mysql-connector-j-9.5.0.jar;." YourMainClass
```

### Permission Errors
If you get permission errors:
- Make sure the MySQL user has proper permissions
- Check the database credentials in `DatabaseConnection.java`

## Database Schema

The database includes the following tables:
- `users` - Base user information
- `admins` - Administrator accounts
- `doctors` - Doctor information
- `patients` - Patient information
- `appointments` - Appointment bookings
- `medical_records` - Medical records
- `prescriptions` - Prescriptions
- `prescription_medications` - Prescription medications

See `create_database.sql` for the complete schema definition.

## Files Modified/Added

### New Files:
- `src/util/DatabaseConnection.java` - Database connection manager
- `src/service/DatabaseService.java` - Database operations service
- `DATABASE_SETUP.md` - This documentation file

### Modified Files:
- `src/util/DataStore.java` - Integrated database service
  - Added database loading on startup
  - Added database saving when adding users
  - Enabled database integration (useDatabase = true)

## Disabling Database (Optional)

If you want to use in-memory storage only (for testing):
In `src/util/DataStore.java`, change:
```java
private boolean useDatabase = false; // Change to false
```

This will disable database operations and use only in-memory storage.
