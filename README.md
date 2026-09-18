# Park.Me

## Smart Parking Management System

Park.Me is a Java Swing desktop application developed as a university Java OOP and GUI project. The system provides a simple solution for managing users, vehicle entry and checkout, parking slots, parking fees, registration numbers, and parking records.

---

## Features

### Authentication
- Admin and User login
- Password-protected accounts
- Separate Admin and User menus
- Admin credential change
- Logout functionality
- Enter key support for login

### User Management
Administrators can:
- Create new users
- Edit existing users
- Delete users
- Validate usernames
- Prevent duplicate usernames
- Validate password strength

Password strength requirements:
- Minimum 6 characters
- Uppercase letter
- Lowercase letter
- Number
- Special character

### Vehicle Entry
The system supports:
- Car
- Truck
- Bus
- Bike

Registration numbers are created from:
- Area
- Initial
- Six-digit number

Example:

```text
DHA KA 123456
```

The entry system:
1. Validates the registration number.
2. Checks whether the vehicle is already parked.
3. Finds the first available parking slot.
4. Assigns the slot.
5. Records the entry time.
6. Saves the parking record.
7. Displays an entry receipt.

### Vehicle Checkout
Users can search for currently parked vehicles using the registration number.

The checkout system:
- Finds the active parking record
- Shows vehicle information
- Calculates parking duration
- Calculates the parking fee
- Shows a checkout preview
- Releases the allocated parking slot
- Records the checkout time
- Stores the final bill
- Provides a printable receipt

### Parking Slot Management
Parking slots are managed separately for every vehicle type.

Default capacities:

```text
Car   : 20
Truck : 35
Bus   : 25
Bike  : 10
```

Slot examples:

```text
Car   : C1, C2, C3, ...
Truck : T1, T2, T3, ...
Bus   : BU1, BU2, BU3, ...
Bike  : B1, B2, B3, ...
```

Administrators can:
- View parking slots
- View occupied slots
- View available slots
- Increase capacity
- Reduce capacity
- Relocate vehicles when capacity is reduced

The application prevents reducing a vehicle category below its current number of occupied slots.

### Parking Fee Management
Administrators can edit:
- Basic parking fee
- Hourly parking fee

Fees are maintained separately for Car, Truck, Bus, and Bike.

### Registration Management
Registration numbers use:

```text
Area + Initial + Six-digit Number
```

Available areas and initials are loaded from:

```text
data/areas.txt
data/initials.txt
```

These files can be edited to change the available registration options.

### Manage Data Table
Administrators can view all parking records in a JTable.

Columns:

| Vehicle Type | Registration | In Time | Out Time | Allocated Slot | Bill |
|---|---|---|---|---|---|

Functions include:
- Exact registration search
- Repeated search for duplicate historical records
- Automatic scrolling to the matched row
- Bold highlighting of the matched row
- Update Selected
- Clear All
- Clear Unparked

---

## Record Update Rules

### Checked-in vehicle

A vehicle is considered checked in when:

```text
Out Time = --
```

For a checked-in vehicle:

| Field | Status |
|---|---|
| Vehicle Type | Editable |
| Registration | Editable |
| In Time | Editable |
| Out Time | Locked |
| Allocated Slot | Editable |
| Bill | Locked |

If the vehicle type, registration, or allocated slot changes, the live parking-slot information is updated accordingly.

### Checked-out vehicle

A vehicle is considered checked out when it has an actual Out Time.

For a checked-out vehicle:

| Field | Status |
|---|---|
| Vehicle Type | Editable |
| Registration | Editable |
| In Time | Editable |
| Out Time | Editable |
| Allocated Slot | Locked |
| Bill | Editable |

The allocated slot remains locked because the vehicle is no longer occupying a parking slot.

---

## Date and Time Editing

The update window uses a popup for date and time editing.

Date controls:
- Day
- Month
- Year

Time controls:
- Hour
- Minute
- Second

Each value can be changed using up/down spinner controls.

The selected date is validated before it is accepted.

---

## Search Behavior

Manage Data Table uses an exact registration-number search.

If the same registration appears in multiple records, pressing `Find` repeatedly moves through the matching rows.

Example:

```text
Find #1 -> First matching record
Find #2 -> Second matching record
Find #3 -> Third matching record
Find #4 -> Back to first matching record
```

Each matching row is:
- Selected
- Scrolled into view
- Highlighted in bold

Changing the registration search resets the search cycle.

---

## Clear Operations

### Clear All

`Clear All` removes all parking records after confirmation.

For vehicles that are still parked, their occupied parking slots are released before the records are removed.

### Clear Unparked

`Clear Unparked` removes only records that have already been checked out.

A record is considered unparked when:

```text
Out Time != --
```

Currently parked vehicles remain unchanged.

---

## Validation

The application validates data before saving.

Examples include:
- Required input validation
- Six-digit registration validation
- Duplicate active vehicle validation
- Username validation
- Password strength validation
- Parking capacity validation
- Whole-number bill validation
- Valid date and time
- Out Time cannot be before In Time

Invalid input is reported through Swing warning dialogs.

---

## Project Architecture

The application follows a simple separation of responsibilities:

```text
Model
   |
   v
Manager
   |
   v
UI Panel
   |
   v
Main
```

### Model

Model classes represent the application's data:

```text
User.java
ParkingSlot.java
ParkingFee.java
ParkingRecord.java
```

### Manager

Manager classes handle application data and business operations:

```text
DataStorage.java
UserManager.java
ParkingManager.java
CashManager.java
PasswordValidator.java
RegistrationDataManager.java
ParkingRecordManager.java
```

### UI

UI classes create the Swing interface and respond to user actions:

```text
LoginPanel.java
AdminMainMenu.java
CreateUserPanel.java
UserManagementPanel.java
EditUserPanel.java
ParkingSlotPanel.java
CashValuePanel.java
ChangeAdminCredentialsPanel.java
RegistrationPanel.java
UserMainMenu.java
EntryPanel.java
OutPanel.java
ManageDataTablePanel.java
UpdateParkingRecordPanel.java
```

### Main

`Main.java` is the application entry point.

---

## Project Structure

```text
Park.Me/
|
├── src/
│   └── smartparking/
│       |
│       ├── main/
│       │   └── Main.java
│       |
│       ├── model/
│       │   ├── User.java
│       │   ├── ParkingSlot.java
│       │   ├── ParkingFee.java
│       │   └── ParkingRecord.java
│       |
│       ├── manager/
│       │   ├── DataStorage.java
│       │   ├── UserManager.java
│       │   ├── ParkingManager.java
│       │   ├── CashManager.java
│       │   ├── PasswordValidator.java
│       │   ├── RegistrationDataManager.java
│       │   └── ParkingRecordManager.java
│       |
│       └── ui/
│           ├── LoginPanel.java
│           ├── AdminMainMenu.java
│           ├── CreateUserPanel.java
│           ├── UserManagementPanel.java
│           ├── EditUserPanel.java
│           ├── ParkingSlotPanel.java
│           ├── CashValuePanel.java
│           ├── ChangeAdminCredentialsPanel.java
│           ├── RegistrationPanel.java
│           ├── UserMainMenu.java
│           ├── EntryPanel.java
│           ├── OutPanel.java
│           ├── ManageDataTablePanel.java
│           └── UpdateParkingRecordPanel.java
|
├── data/
│   ├── areas.txt
│   ├── initials.txt
│   └── runtime data files
|
├── README.md
└── .gitignore
```

---

## Technologies Used

- Java
- Java Swing
- Object-Oriented Programming
- Encapsulation
- Inheritance
- Method overriding
- Constructors
- ArrayList
- JFrame
- JPanel
- JLabel
- JTextField
- JPasswordField
- JButton
- JRadioButton
- ButtonGroup
- JComboBox
- JTable
- JScrollPane
- JOptionPane
- Layout Managers
- Java Date and Time API
- Java File I/O
- AES/GCM encrypted local storage

---

## Data Storage

Park.Me stores application data in the `data` folder.

Files used by the application include:

```text
data/users.txt
data/admin.txt
data/slots.txt
data/fees.txt
data/parking.txt
data/areas.txt
data/initials.txt
```

The main runtime records are stored in encrypted form.

The editable registration-option files are:

```text
data/areas.txt
data/initials.txt
```

---

## Security Note

The application uses local encryption for runtime application data.

This encryption is intended for the university desktop project and should not be considered production-grade credential or secret management.

Runtime files can contain local user information, administrator information, parking records, fees, and slot state. For that reason, these runtime files should normally not be uploaded to a public GitHub repository.

---

## First Run

On the first run, Park.Me initializes its required data.

The default administrator account is:

```text
Username: admin
Password: admin
```

After logging in, the administrator can change the credentials from:

```text
Admin Main Menu
    |
    v
Change Admin Credentials
```

---

## Running in IntelliJ IDEA

### Step 1: Open the project

Open the `Park.Me` project folder in IntelliJ IDEA.

### Step 2: Configure Java

Make sure the project has a Java JDK configured.

### Step 3: Open Main.java

Open:

```text
src/smartparking/main/Main.java
```

### Step 4: Run the project

Right-click `Main.java` and select:

```text
Run 'Main.main()'
```

The Park.Me login window should appear.

---

## Terminal Compilation

From the project root, create an output directory:

```bash
mkdir out
```

On Git Bash:

```bash
javac -d out $(find src -name "*.java")
```

The main class is:

```text
smartparking.main.Main
```

Run the application using:

```bash
java -cp out smartparking.main.Main
```

For IntelliJ IDEA, running `Main.java` is the simplest method.

---

## Application Flow

### Admin Flow

```text
Login
  |
  v
Admin Main Menu
  |
  +--> Create New User
  |
  +--> Delete / Edit User
  |
  +--> Manage Data Table
  |
  +--> Manage Parking Slot
  |
  +--> Edit Cash Values
  |
  +--> Change Admin Credentials
  |
  +--> Logout
```

### User Flow

```text
Login
  |
  v
User Main Menu
  |
  +--> Entry
  |
  +--> Out
  |
  +--> Logout
```

---

## Vehicle Entry Flow

```text
Select Vehicle Type
        |
        v
Select Area
        |
        v
Select Initial
        |
        v
Enter Six-digit Number
        |
        v
Validate Registration
        |
        v
Check Active Parking Record
        |
        v
Find Available Parking Slot
        |
        v
Occupy Slot
        |
        v
Create Parking Record
        |
        v
Save Data
```

---

## Vehicle Checkout Flow

```text
Enter Registration
        |
        v
Find Active Record
        |
        v
Calculate Parking Duration
        |
        v
Calculate Parking Fee
        |
        v
Show Checkout Preview
        |
        v
Print / Confirm Checkout
        |
        v
Release Parking Slot
        |
        v
Update Parking Record
        |
        v
Save Data
```

---

## Billing

Every vehicle type has a basic fee and an hourly fee.

The checkout process calculates parking duration using the recorded entry time and checkout time.

The resulting parking duration is then used with the configured vehicle-type fees to calculate the final bill.

Administrators can change the fee values through:

```text
Admin Main Menu
    |
    v
Edit Cash Values
```

---

## Group Members and Contributions

| Name | Student ID | Contribution |
|---|---|---|
| **Indra Das** | 2024100000549 | **Core system development:** overall project structure, authentication and login system, user management, parking entry and checkout logic, data storage and encryption, parking record management, Manage Data Table, record update functionality, validation, and integration of the main system components. |
| **Shah Alam Khan Saad** | 2024100000539 | **Parking management module:** parking slot management, vehicle-type slot allocation, capacity management, occupied/available slot handling, capacity reduction and vehicle relocation, and integration with parking records. |
| **Sabrina Tabassum** | 2025000000292 | **Fee and administrative features:** parking fee/cash management, admin menu features, fee validation, and support for administrative settings and system configuration. |
| **Sanjida Hossain Anny** | 2024100000555 | **Registration and user-interface features:** vehicle registration handling, area and initial selection, registration validation, and supporting Swing UI components and layouts. |
| **Md Sefat Ullah** | 2024100000558 | **Basic UI and testing support:** assisting with simple Swing interface components, input/output screens, basic testing, bug checking, and documentation/support tasks. |

---

## Project Status

**Completed**

Park.Me currently provides the main functionality of a Smart Parking Management System, including:

- Authentication
- User management
- Vehicle entry
- Vehicle checkout
- Parking slot management
- Parking fee management
- Registration management
- Persistent data storage
- Encrypted runtime storage
- JTable-based record management
- Exact record search
- Repeated-match search
- Record update
- Clear All
- Clear Unparked
- Input validation

---

## Academic Project

**Team Name:** Anubis  
**Project Name:** Park.Me  
**Project Type:** Smart Parking Management System  
**Language:** Java  
**GUI Framework:** Java Swing  
**Platform:** Desktop Application

Developed as a university Java OOP and GUI project.
