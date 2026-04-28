import model.ClassRoom;
import model.Subject;
import model.Teacher;
import model.TimeSlot;
import timetable.Timetable;
import timetable.TimetableGenerator;
import util.TimetableExporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ============================================================
 *  TIMETABLE GENERATOR — Main Entry Point
 * ============================================================
 *
 *  This class drives the entire application:
 *    1. Offers a pre-configured demo mode (quick start)
 *    2. Offers an interactive mode where the user enters data
 *    3. Generates the timetable via TimetableGenerator
 *    4. Displays results in console (compact + detailed views)
 *    5. Optionally exports to TXT / CSV
 *
 *  Run with:  javac -d out -sourcepath src src/Main.java
 *             java  -cp out Main
 * ============================================================
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printBanner();

        System.out.println("Choose mode:");
        System.out.println("  1. Demo mode  (pre-configured sample data)");
        System.out.println("  2. Interactive mode (enter your own data)");
        System.out.print("Enter choice [1/2]: ");

        String choice = scanner.nextLine().trim();

        if (choice.equals("2")) {
            runInteractiveMode(scanner);
        } else {
            runDemoMode();
        }

        scanner.close();
    }

    // ---------------------------------------------------------------
    // Demo Mode — pre-configured data, no user input required
    // ---------------------------------------------------------------

    private static void runDemoMode() {
        System.out.println("\n[Demo Mode] Using pre-configured sample data...\n");

        // --- Subjects ---
        Subject math    = new Subject("Mathematics",        "MATH", 5);
        Subject physics = new Subject("Physics",            "PHY",  4);
        Subject chem    = new Subject("Chemistry",          "CHEM", 3);
        Subject english = new Subject("English",            "ENG",  4);
        Subject cs      = new Subject("Computer Science",   "CS",   4);
        Subject history = new Subject("History",            "HIST", 3);
        Subject pe      = new Subject("Physical Education", "PE",   2);

        List<Subject> subjects = List.of(math, physics, chem, english, cs, history, pe);

        // --- Teachers ---
        Teacher tMath    = new Teacher("Alice Johnson",  "T001", 4);
        Teacher tPhyChem = new Teacher("Bob Smith",      "T002", 4);
        Teacher tEng     = new Teacher("Carol White",    "T003", 4);
        Teacher tCS      = new Teacher("David Lee",      "T004", 4);
        Teacher tHistPE  = new Teacher("Eva Brown",      "T005", 4);

        // Assign subjects to teachers
        tMath.assignSubject(math);
        tPhyChem.assignSubject(physics);
        tPhyChem.assignSubject(chem);
        tEng.assignSubject(english);
        tCS.assignSubject(cs);
        tHistPE.assignSubject(history);
        tHistPE.assignSubject(pe);

        List<Teacher> teachers = List.of(tMath, tPhyChem, tEng, tCS, tHistPE);

        // --- Classrooms ---
        List<ClassRoom> rooms = List.of(
                new ClassRoom("R101", 40, false),
                new ClassRoom("R102", 40, false),
                new ClassRoom("LAB1", 30, true),
                new ClassRoom("R103", 35, false)
        );

        // --- Days & Time Slots ---
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        TimeSlot[] slots = {
                new TimeSlot(1, "08:00", "09:00"),
                new TimeSlot(2, "09:00", "10:00"),
                new TimeSlot(3, "10:00", "11:00"),
                new TimeSlot(4, "11:00", "12:00"),
                new TimeSlot(5, "13:00", "14:00"),
                new TimeSlot(6, "14:00", "15:00"),
                new TimeSlot(7, "15:00", "16:00")
        };

        // --- Generate ---
        generateAndDisplay(subjects, teachers, rooms, days, slots);
    }

    // ---------------------------------------------------------------
    // Interactive Mode — user enters all data via console
    // ---------------------------------------------------------------

    private static void runInteractiveMode(Scanner scanner) {
        System.out.println("\n[Interactive Mode]\n");

        // --- Subjects ---
        List<Subject> subjects = new ArrayList<>();
        System.out.print("How many subjects? ");
        int numSubjects = readPositiveInt(scanner);

        for (int i = 0; i < numSubjects; i++) {
            System.out.println("\nSubject " + (i + 1) + ":");
            System.out.print("  Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("  Code (e.g. MATH): ");
            String code = scanner.nextLine().trim().toUpperCase();
            System.out.print("  Periods per week: ");
            int periods = readPositiveInt(scanner);
            subjects.add(new Subject(name, code, periods));
        }

        // --- Teachers ---
        List<Teacher> teachers = new ArrayList<>();
        System.out.print("\nHow many teachers? ");
        int numTeachers = readPositiveInt(scanner);

        for (int i = 0; i < numTeachers; i++) {
            System.out.println("\nTeacher " + (i + 1) + ":");
            System.out.print("  Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("  ID (e.g. T001): ");
            String id = scanner.nextLine().trim();
            System.out.print("  Max periods per day: ");
            int maxPeriods = readPositiveInt(scanner);

            Teacher teacher = new Teacher(name, id, maxPeriods);

            // Assign subjects to this teacher
            System.out.println("  Which subjects does this teacher teach?");
            for (int j = 0; j < subjects.size(); j++) {
                System.out.print("    Assign " + subjects.get(j).getSubjectName() + "? [y/n]: ");
                String ans = scanner.nextLine().trim().toLowerCase();
                if (ans.equals("y")) {
                    teacher.assignSubject(subjects.get(j));
                }
            }
            teachers.add(teacher);
        }

        // --- Classrooms (optional) ---
        List<ClassRoom> rooms = new ArrayList<>();
        System.out.print("\nAdd classrooms? [y/n]: ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.print("How many classrooms? ");
            int numRooms = readPositiveInt(scanner);
            for (int i = 0; i < numRooms; i++) {
                System.out.println("\nClassroom " + (i + 1) + ":");
                System.out.print("  Room number: ");
                String roomNum = scanner.nextLine().trim();
                System.out.print("  Capacity: ");
                int cap = readPositiveInt(scanner);
                System.out.print("  Is it a lab? [y/n]: ");
                boolean isLab = scanner.nextLine().trim().equalsIgnoreCase("y");
                rooms.add(new ClassRoom(roomNum, cap, isLab));
            }
        }

        // --- Days ---
        System.out.print("\nInclude Saturday? [y/n]: ");
        boolean includeSaturday = scanner.nextLine().trim().equalsIgnoreCase("y");
        String[] days = includeSaturday
                ? new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}
                : new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        // --- Time Slots ---
        System.out.print("How many periods per day? (max 8): ");
        int numSlots = Math.min(readPositiveInt(scanner), 8);

        TimeSlot[] slots = buildDefaultTimeSlots(numSlots);

        // --- Generate ---
        generateAndDisplay(subjects, teachers, rooms, days, slots);
    }

    // ---------------------------------------------------------------
    // Shared generation + display logic
    // ---------------------------------------------------------------

    private static void generateAndDisplay(List<Subject> subjects,
                                            List<Teacher> teachers,
                                            List<ClassRoom> rooms,
                                            String[] days,
                                            TimeSlot[] slots) {
        System.out.println("\nGenerating timetable...");

        TimetableGenerator generator = new TimetableGenerator(subjects, teachers, rooms, days, slots);
        Timetable timetable = generator.generateTimetable();

        // Compact table view
        System.out.println("\n========== WEEKLY TIMETABLE (COMPACT) ==========");
        timetable.displayTimetable();

        // Detailed view
        timetable.displayDetailedTimetable();

        // Workload report
        generator.printWorkloadReport(timetable);

        // Export options
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("\nExport timetable to file? [y/n]: ");
            if (sc.hasNextLine() && sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.print("  Export as TXT? [y/n]: ");
                if (sc.hasNextLine() && sc.nextLine().trim().equalsIgnoreCase("y")) {
                    TimetableExporter.exportToTxt(timetable, "timetable.txt");
                }
                System.out.print("  Export as CSV? [y/n]: ");
                if (sc.hasNextLine() && sc.nextLine().trim().equalsIgnoreCase("y")) {
                    TimetableExporter.exportToCsv(timetable, "timetable.csv");
                }
            }
        } catch (Exception e) {
            // No interactive terminal available — skip export prompt silently
        }

        System.out.println("\nDone. Thank you for using Timetable Generator!");
    }

    // ---------------------------------------------------------------
    // Utility helpers
    // ---------------------------------------------------------------

    /**
     * Builds a default set of time slots starting at 08:00,
     * each one hour long.
     */
    private static TimeSlot[] buildDefaultTimeSlots(int count) {
        TimeSlot[] slots = new TimeSlot[count];
        int startHour = 8;
        for (int i = 0; i < count; i++) {
            // Insert a 1-hour lunch break after slot 4 (12:00–13:00)
            if (startHour == 12) startHour = 13;
            String start = String.format("%02d:00", startHour);
            String end   = String.format("%02d:00", startHour + 1);
            slots[i] = new TimeSlot(i + 1, start, end);
            startHour++;
        }
        return slots;
    }

    /**
     * Reads a positive integer from the scanner, re-prompting on invalid input.
     */
    private static int readPositiveInt(Scanner scanner) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) return value;
                System.out.print("  Please enter a positive number: ");
            } catch (NumberFormatException e) {
                System.out.print("  Invalid input. Enter a number: ");
            }
        }
    }

    private static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║        TIMETABLE GENERATOR  v1.0             ║");
        System.out.println("║   Automated Weekly Class Schedule System     ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}
