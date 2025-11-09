# Run Guide — MediConnect+ (Java)

## Prerequisites

- Java 8+ (JDK)
- Windows PowerShell or Command Prompt
- Optional database: MySQL (XAMPP includes MySQL)

## Optional: Database Setup (MySQL)

1. Start MySQL from the XAMPP Control Panel.
2. Create schema by importing the SQL file:
   - Using phpMyAdmin: Import `create_database.sql`.
   - Or via terminal:
     ```bash
     mysql -u root -p < create_database.sql
     ```
3. Verify credentials in `src\util\DatabaseConnection.java`:
   - URL: `jdbc:mysql://localhost:3306/mediconnect_db`
   - User: `root`
   - Password: (empty by default)
4. When running with DB, include the connector on the classpath: `mysql-connector-j-9.5.0.jar` (already in project root).

Note: If the DB/driver is unavailable, the app automatically falls back to in-memory mode.

## Compile (Windows)

From the project root:

```powershell
javac -d . src\Main.java src\model\*.java src\service\*.java src\ui\*.java src\ui\swing\*.java src\util\*.java
```

## Run (Console UI)

- In-memory (no MySQL required):
  ```powershell
  java Main --console
  ```

- With MySQL (ensure MySQL is running):
  ```powershell
  java -cp ".;mysql-connector-j-9.5.0.jar" Main --console
  ```

## Run (Swing UI)

- In-memory:
  ```powershell
  java ui.swing.MediConnectSwingApp
  ```

- With MySQL:
  ```powershell
  java -cp ".;mysql-connector-j-9.5.0.jar" ui.swing.MediConnectSwingApp
  ```

## Mode Selection (from Main)

- If you run without `--console` or `--swing`, `Main` shows a dialog to choose between Swing and Console.

## Troubleshooting

- "MySQL Driver not found": add the connector jar to the classpath when running, e.g., `-cp ".;mysql-connector-j-9.5.0.jar"`.
- "Access denied" or connection errors: check credentials/URL in `src\util\DatabaseConnection.java` and that MySQL is running; ensure the schema exists.
- ANSI colors look odd in the console: this is cosmetic; you can ignore it or use Windows Terminal/PowerShell (VT sequences supported).

## End-to-End Commands (Windows PowerShell) — With Database

Follow these exact commands from a PowerShell window to compile and run with MySQL enabled.

1) Go to project folder

```powershell
cd "C:\xampp\htdocs\FINALPROJ_Advanced OOP"
```

2) Verify Java is installed

```powershell
java -version
javac -version
```

3) Start MySQL (use XAMPP Control Panel) and verify connection (optional)

- If `mysql` is in PATH:
  ```powershell
  mysql -u root -p -e "SELECT VERSION();"
  ```
- If not, run from XAMPP MySQL bin folder (adjust path if needed):
  ```powershell
  & "C:\xampp\mysql\bin\mysql.exe" -u root -p -e "SELECT VERSION();"
  ```

4) Create schema by importing SQL

```powershell
mysql -u root -p < create_database.sql
```

5) Compile the project

```powershell
javac -d . src\Main.java src\model\*.java src\service\*.java src\ui\*.java src\ui\swing\*.java src\util\*.java
```

6) Run in Console mode with MySQL (connector jar on classpath)

```powershell
java -cp ".;mysql-connector-j-9.5.0.jar" Main --console
```

7) (Alternative) Run in Swing mode with MySQL

```powershell
java -cp ".;mysql-connector-j-9.5.0.jar" ui.swing.MediConnectSwingApp
```

Notes:
- Default DB credentials are configured in `src\util\DatabaseConnection.java` (user `root`, empty password). If your MySQL has a password, update that file or create a `root` account that matches.
- Ensure `mysql-connector-j-9.5.0.jar` is present in the project root; it is included with this project.

javac -d . src\Main.java src\model\*.java src\service\*.java src\ui\*.java src\ui\swing\*.java src\util\*.java
java -cp ".;mysql-connector-j-9.5.0.jar" ui.swing.MediConnectSwingApp