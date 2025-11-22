# Dynamic Bus Schedule (JavaFX + SQLite, Maven, Java 17)

## How to run
1. Open this folder in IntelliJ IDEA.
2. Make sure you have **JDK 17** installed and set as the project SDK.
3. Let Maven download dependencies.
4. Run the Maven plugin target:
   - In IntelliJ: open **Maven** tool window → **Plugins** → **javafx** → **javafx:run**.
   - Or from terminal: `mvn clean javafx:run`.

## Accounts
- Admin: `admin` / `admin123`
- Student: `student1` / `stud123`

## Notes
- On first run, the SQLite DB file `bus_schedule.db` is created in the project root automatically with sample data.
- Admin can add or remove buses; students can only view.
