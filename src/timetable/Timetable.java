package timetable;

import model.ClassRoom;
import model.ScheduleEntry;
import model.Subject;
import model.Teacher;
import model.TimeSlot;

import java.util.List;

/**
 * Core timetable data structure.
 * Holds a 2D grid: [day][slot] → ScheduleEntry
 * Also exposes conflict-checking and display logic.
 */
public class Timetable {

    // Days of the week (0=Monday … 4=Friday, or up to 5=Saturday)
    private final String[] days;

    // Time slots for each day
    private final TimeSlot[] timeSlots;

    // The 2D schedule matrix [dayIndex][slotIndex]
    private final ScheduleEntry[][] schedule;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public Timetable(String[] days, TimeSlot[] timeSlots) {
        if (days == null || days.length == 0) {
            throw new IllegalArgumentException("Days array cannot be null or empty.");
        }
        if (timeSlots == null || timeSlots.length == 0) {
            throw new IllegalArgumentException("TimeSlots array cannot be null or empty.");
        }
        this.days = days;
        this.timeSlots = timeSlots;
        this.schedule = new ScheduleEntry[days.length][timeSlots.length];
    }

    // ---------------------------------------------------------------
    // Core Methods
    // ---------------------------------------------------------------

    /**
     * Assigns a subject/teacher/room to a specific cell.
     * Returns true if the assignment was successful, false if a conflict exists.
     */
    public boolean assignSubject(int dayIndex, int slotIndex,
                                  Subject subject, Teacher teacher, ClassRoom room) {
        if (!isValidIndex(dayIndex, slotIndex)) {
            System.err.println("Invalid day/slot index: " + dayIndex + "/" + slotIndex);
            return false;
        }
        if (checkConflicts(dayIndex, slotIndex, teacher)) {
            return false; // conflict detected — skip this assignment
        }
        schedule[dayIndex][slotIndex] = new ScheduleEntry(subject, teacher, room);
        return true;
    }

    /**
     * Checks whether assigning the given teacher to [dayIndex][slotIndex]
     * would create a conflict (teacher already busy in that slot on that day).
     *
     * @return true if a conflict exists, false if the slot is safe
     */
    public boolean checkConflicts(int dayIndex, int slotIndex, Teacher teacher) {
        if (teacher == null) return false;

        // A teacher cannot be in two places at the same time slot on the same day
        ScheduleEntry existing = schedule[dayIndex][slotIndex];
        if (existing != null && existing.getTeacher() != null) {
            if (existing.getTeacher().getTeacherId().equals(teacher.getTeacherId())) {
                return true; // same teacher already assigned here
            }
        }
        return false;
    }

    /**
     * Checks if the same subject appears in the immediately preceding slot
     * on the same day (used to avoid back-to-back repetition).
     */
    public boolean isSameSubjectInPreviousSlot(int dayIndex, int slotIndex, Subject subject) {
        if (slotIndex == 0) return false;
        ScheduleEntry prev = schedule[dayIndex][slotIndex - 1];
        if (prev == null || prev.getSubject() == null) return false;
        return prev.getSubject().getSubjectCode().equals(subject.getSubjectCode());
    }

    /**
     * Counts how many times a subject has been scheduled on a given day.
     */
    public int countSubjectOnDay(int dayIndex, Subject subject) {
        int count = 0;
        for (int s = 0; s < timeSlots.length; s++) {
            ScheduleEntry entry = schedule[dayIndex][s];
            if (entry != null && entry.getSubject() != null
                    && entry.getSubject().getSubjectCode().equals(subject.getSubjectCode())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts how many times a subject has been scheduled across the entire week.
     */
    public int countSubjectInWeek(Subject subject) {
        int count = 0;
        for (int d = 0; d < days.length; d++) {
            count += countSubjectOnDay(d, subject);
        }
        return count;
    }

    /**
     * Counts how many periods a teacher has been assigned on a given day.
     */
    public int countTeacherPeriodsOnDay(int dayIndex, Teacher teacher) {
        int count = 0;
        for (int s = 0; s < timeSlots.length; s++) {
            ScheduleEntry entry = schedule[dayIndex][s];
            if (entry != null && entry.getTeacher() != null
                    && entry.getTeacher().getTeacherId().equals(teacher.getTeacherId())) {
                count++;
            }
        }
        return count;
    }

    // ---------------------------------------------------------------
    // Display
    // ---------------------------------------------------------------

    /**
     * Prints the timetable to the console in a formatted table.
     * Rows = time slots, Columns = days.
     */
    public void displayTimetable() {
        int colWidth = 14; // width of each day column
        int timeColWidth = 12;

        // Build separator line
        int totalWidth = timeColWidth + (colWidth * days.length) + days.length + 1;
        String separator = "-".repeat(totalWidth);

        System.out.println("\n" + separator);
        System.out.printf("%-" + timeColWidth + "s", "Time");
        for (String day : days) {
            System.out.printf("| %-" + (colWidth - 1) + "s", day);
        }
        System.out.println("|");
        System.out.println(separator);

        for (int s = 0; s < timeSlots.length; s++) {
            // Time column
            System.out.printf("%-" + timeColWidth + "s", timeSlots[s].display());

            // Day columns
            for (int d = 0; d < days.length; d++) {
                ScheduleEntry entry = schedule[d][s];
                String cell = (entry != null) ? entry.toDisplayString() : "  FREE  ";
                System.out.printf("| %-" + (colWidth - 1) + "s", cell);
            }
            System.out.println("|");
        }
        System.out.println(separator);
    }

    /**
     * Prints a detailed view showing subject name + teacher name per cell.
     */
    public void displayDetailedTimetable() {
        System.out.println("\n========== DETAILED TIMETABLE ==========");
        for (int d = 0; d < days.length; d++) {
            System.out.println("\n--- " + days[d] + " ---");
            for (int s = 0; s < timeSlots.length; s++) {
                ScheduleEntry entry = schedule[d][s];
                if (entry != null && entry.getSubject() != null) {
                    String room = (entry.getRoom() != null)
                            ? " | Room: " + entry.getRoom().getRoomNumber()
                            : "";
                    System.out.printf("  %s  →  %-15s  Teacher: %-15s%s%n",
                            timeSlots[s].display(),
                            entry.getSubject().getSubjectName(),
                            (entry.getTeacher() != null ? entry.getTeacher().getTeacherName() : "N/A"),
                            room);
                } else {
                    System.out.printf("  %s  →  FREE%n", timeSlots[s].display());
                }
            }
        }
        System.out.println("\n=========================================");
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private boolean isValidIndex(int dayIndex, int slotIndex) {
        return dayIndex >= 0 && dayIndex < days.length
                && slotIndex >= 0 && slotIndex < timeSlots.length;
    }

    public ScheduleEntry getEntry(int dayIndex, int slotIndex) {
        return schedule[dayIndex][slotIndex];
    }

    public String[] getDays() {
        return days;
    }

    public TimeSlot[] getTimeSlots() {
        return timeSlots;
    }

    public ScheduleEntry[][] getSchedule() {
        return schedule;
    }
}
