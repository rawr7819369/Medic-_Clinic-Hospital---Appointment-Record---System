# 🩺 MediConnect+ Smart Medical Appointment System

> **Advanced Object-Oriented Programming Final Project**  
> **Language:** Java  
> **Paradigm:** Full Object-Oriented Programming  
> **Storage:** MySQL Database with In-Memory Caching  

## 📋 Project Overview

MediConnect+ is a comprehensive medical appointment management system designed to demonstrate advanced OOP principles including Encapsulation, Inheritance, Polymorphism, and Abstraction. The system provides role-based access for Administrators, Doctors, and Patients with a focus on accessibility and user experience.

## 🏗️ System Architecture

### Package Structure
```
src/
├── model/          # Data models and entities
├── service/        # Business logic services
├── ui/             # User interface screens
├── util/           # Utility classes
└── Main.java       # Application entry point
```

### Core Components

#### Model Classes (OOP Implementation)
- **`User`** (Abstract) - Base class for all user types
- **`Admin`** - System administrators
- **`Doctor`** - Medical professionals
- **`Patient`** - Patients
- **`Appointment`** - Appointment management
- **`MedicalRecord`** - Medical records
- **`Prescription`** - Prescription management

#### Service Classes (Business Logic)
- **`LoginService`** - Authentication and user management
- **`RegistrationService`** - User registration
- **`AppointmentService`** - Appointment operations
- **`ReportService`** - Report generation
- **`AccessibilityService`** - Accessibility features

#### UI Classes (User Interface)
- **`LoginScreen`** - Login and registration
- **`AdminDashboard`** - Administrator interface
- **`DoctorDashboard`** - Doctor interface
- **`PatientHomeScreen`** - Patient interface

#### Utility Classes
- **`DataStore`** - In-memory data management with database integration
- **`DatabaseConnection`** - MySQL database connection manager
- **`InputValidator`** - Input validation
- **`ConsolePrinter`** - Formatted console output

#### Database Service
- **`DatabaseService`** - CRUD operations for all entities

## 🚀 Features

### Core Functionality
- **User Authentication** - Role-based login system
- **Appointment Management** - Booking, viewing, cancellation
- **Medical Records** - Patient history and records
- **Prescription Management** - Medication tracking
- **Report Generation** - System and appointment reports

### Accessibility Features
- **Font Size Adjustment** - Small, Medium, Large, Extra Large
- **Theme Selection** - Light, Dark, High Contrast, Blue, Green
- **Text-to-Speech** - Audio assistance
- **Voice Assistance** - Voice commands
- **Large Buttons** - Enhanced interaction
- **Screen Reader** - Audio descriptions

### User Roles

#### Administrator
- Manage doctors and patients
- View all appointments
- Generate system reports
- System statistics and oversight

#### Doctor
- View appointment schedule
- Manage patient records
- Create prescriptions
- Medical record management

#### Patient
- Book appointments
- View medical history
- Profile management
- Appointment tracking

## 🛠️ Installation & Setup

### Prerequisites
- Java 8 or higher
- MySQL Server (XAMPP includes this)
- Command line interface

### Database Setup
Before running the application, set up the MySQL database:
1. Start MySQL in XAMPP Control Panel
2. Import the database schema:
   ```bash
   mysql -u root -p < create_database.sql
   ```
   Or use phpMyAdmin to import `create_database.sql`
3. The application will automatically connect to the database on startup

See `DATABASE_SETUP.md` for detailed database setup instructions.

### Running the Application

1. **Compile the application:**
   ```bash
   javac -d . src/*.java src/model/*.java src/service/*.java src/ui/*.java src/util/*.java
   ```

2. **Run the application:**
   ```bash
   java src.Main
   ```

### Default Login Credentials

#### Administrator
- **Username:** `admin`
- **Password:** `Admin123!`
- **Role:** Admin

#### Doctor
- **Username:** `doctor`
- **Password:** `Doctor123!`
- **Role:** Doctor

#### Patient
- **Username:** `patient`
- **Password:** `Patient123!`
- **Role:** Patient

## 📱 Screen Flow

### 1. Login Screen
- Username and password authentication
- Role selection (Admin, Doctor, Patient)
- Registration for new patients
- Forgot password functionality
- Accessibility settings

### 2. Registration Screen (Patient)
- Personal information collection
- Medical information (age, gender, blood type)
- Emergency contact details
- Accessibility options

### 3. Admin Dashboard
- Doctor management (add, view, remove)
- Patient management (view, approve, deactivate)
- Appointment oversight
- Report generation
- System statistics

### 4. Doctor Dashboard
- Schedule management
- Patient list and records
- Medical record creation
- Prescription management
- Appointment handling

### 5. Patient Home Screen
- Appointment booking
- Medical history access
- Profile management
- Appointment tracking

## 🔧 OOP Principles Implementation

### Encapsulation
- Private fields with public getters/setters
- Data validation in model classes
- Service layer abstraction

### Inheritance
- `User` abstract base class
- `Admin`, `Doctor`, `Patient` inheritance
- Method overriding and polymorphism

### Polymorphism
- Interface-based service design
- Method overriding in subclasses
- Runtime method resolution

### Abstraction
- Abstract `User` class
- Service layer abstraction
- UI component abstraction

## 🧪 Testing

### Manual Testing
1. **Login Testing** - Test all user roles
2. **Appointment Testing** - Book, view, cancel appointments
3. **Medical Records** - Create and view records
4. **Accessibility** - Test all accessibility features
5. **Report Generation** - Generate various reports

### Test Scenarios
- User registration and login
- Appointment booking and management
- Medical record creation and viewing
- Report generation and export
- Accessibility feature testing

## 📊 System Statistics

The system tracks and displays:
- Total users (Admins, Doctors, Patients)
- Appointment statistics
- Medical record counts
- Prescription tracking
- System performance metrics

## 🎯 Key Features Demonstrated

### Object-Oriented Design
- **Abstract Classes** - User base class
- **Inheritance** - Role-based user types
- **Polymorphism** - Dynamic method resolution
- **Encapsulation** - Data protection and validation

### Design Patterns
- **Singleton Pattern** - DataStore and AccessibilityService
- **Service Layer Pattern** - Business logic separation
- **MVC Pattern** - Model-View-Controller architecture

### Accessibility Compliance
- **WCAG Guidelines** - Web Content Accessibility Guidelines
- **Screen Reader Support** - Audio descriptions
- **Keyboard Navigation** - Full keyboard support
- **High Contrast Mode** - Visual accessibility

## 📝 Documentation

### Code Documentation
- Comprehensive JavaDoc comments
- Method and class documentation
- Parameter and return value descriptions
- Usage examples and notes

### User Manual
- Step-by-step usage instructions
- Feature explanations
- Troubleshooting guide
- Accessibility feature guide

## 🔮 Future Enhancements

### Potential Improvements
- Database integration (MySQL, PostgreSQL)
- Web-based interface (Spring Boot)
- Mobile application (Android/iOS)
- Real-time notifications
- Advanced reporting and analytics
- Integration with medical devices

### Technical Enhancements
- Unit testing framework (JUnit)
- Integration testing
- Performance optimization
- Security enhancements
- API development

## 👥 Development Team

**MediConnect+ Development Team**
- Advanced Object-Oriented Programming
- Java Development
- System Architecture Design
- User Experience Design

## 📄 License

This project is developed for educational purposes as part of the Advanced Object-Oriented Programming course.

## 🤝 Contributing

This is an academic project. For educational purposes only.

---

**MediConnect+ Smart Medical Appointment System**  
*Advanced Object-Oriented Programming Final Project*  
*Version 1.0 - Java Implementation*


cd "OneDrive\Documents\FINALPROJ_Advanced OOP"
javac -d . src\Main.java src\SimpleMain.java src\TestSwingUI.java src\model\*.java src\service\*.java src\ui\*.java src\ui\swing\*.java src\util\*.java
java Main#   M e d i c - _ C l i n i c - H o s p i t a l - - - A p p o i n t m e n t - R e c o r d - - - S y s t e m  
 