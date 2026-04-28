package timetable;

import model.ClassRoom;
import model.Subject;
import model.Teacher;
import model.TimeSlot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Responsible for generating a valid weekly timetable.
 *
 * Algorithm overview:
 * 1. Build a "pool" of (subject, teacher) pairs weighted by periodsPerWeek.
 * 2. Shuffle the pool to introduce randomness.
 * 3. Iterate over every (day, slot) cell in the grid.
 * 4. For each cell, pick the next candidate from the pool that satisfies:
 *    a. Teacher is not already assigned in this slot on this day.
 *    b. Teacher has not exceeded their daily workload cap.
 *    c. The same subject does not appear in the immediately preceding slot.
 *    d. The subject has not already reached its weekly quota.
 * 5. If no candidate fits, leave the cell as FREE.
 */
public class TimetableGenerator {

    private final List<Subject> subjects;
    private final List<Teacher> teachers;
    private final List<ClassRoom> rooms;
    private final String[] days;
    private final TimeSlot[] timeSlots;
    private final Random random;

    // Maps subject code → teacher responsible for it
    private final Map<String, Teacher> subjectTeacherMap;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public TimetableGenerator(List<Subject> subjects,
                               List<Teacher> teachers,
                               List<ClassRoom> rooms,
                               String[] days,
                               TimeSlot[] timeSlots) {
        if (subjects == null || subjects.isEmpty()) {
            throw new IllegalArgumentException("Subject list cannot be null or empty.");
        }
        if (teachers == null || teachers.isEmpty()) {
            throw new IllegalArgumentException("Teacher list cannot be null or empty.");
        }
        this.subjects = subjects;
        this.teachers = teachers;
        this.rooms = (rooms != null) ? rooms : new ArrayList<>();
        this.days = days;
        this.timeSlots = timeSlots;
        this.random = new Random();
        this.subjectTeacherMap = new HashMap<>();

        buildSubjectTeacherMap();
    }

    // ---------------------------------------------------------------
    // Public Entry Point
    // ---------------------------------------------------------------

    /**
     * Generates and returns a fully populated Timetable.
     */
    public Timetable generateTimetable() {
        Timetable timetable = new Timetable(days, timeSlots);

        // Build a weighted, shuffled pool of subjects to assign
        List<Subject> pool = buildSubjectPool();

        // Track how many times each subject has been placed this week
        Map<String, Integer> weeklyCount = new HashMap<>();
        for (Subject s : subjects) {
            weeklyCount.put(s.getSubjectCode(), 0);
        }

        // Iterate day by day, slot by slot
        for (int d = 0; d < days.length; d++) {
            // Track teacher periods per day to enforce workload cap
            Map<String, Integer> teacherDayCount = new HashMap<>();

            for (int s = 0; s < timeSlots.length; s++) {
                // Try to find a valid subject for this cell
                Subject chosen = pickSubject(timetable, pool, weeklyCount, teacherDayCount, d, s);

                if (chosen != null) {
                    Teacher teacher = subjectTeacherMap.get(chosen.getSubjectCode());
                    ClassRoom room = pickRoom();

                    boolean assigned = timetable.assignSubject(d, s, chosen, teacher, room);
                    if (assigned) {
                        // Update weekly and daily counters
                        weeklyCount.merge(chosen.getSubjectCode(), 1, Integer::sum);
                        if (teacher != null) {
                            teacherDayCount.merge(teacher.getTeacherId(), 1, Integer::sum);
                        }
                    }
                }
                // If chosen == null, the cell stays FREE
            }
        }

        return timetable;
    }

    // ---------------------------------------------------------------
    // Private Helpers
    // ---------------------------------------------------------------

    /**
     * Builds a weighted list of subjects: each subject appears
     * proportional to its periodsPerWeek value, then shuffles it.
     */
    private List<Subject> buildSubjectPool() {
        List<Subject> pool = new ArrayList<>();
        for (Subject subject : subjects) {
            for (int i = 0; i < subject.getPeriodsPerWeek(); i++) {
                pool.add(subject);
            }
        }
        Collections.shuffle(pool, random);
        return pool;
    }

    /**
     * Picks the best subject for a given (day, slot) cell.
     * Iterates through the shuffled pool and returns the first valid candidate.
     */
    private Subject pickSubject(Timetable timetable,
                                 List<Subject> pool,
                                 Map<String, Integer> weeklyCount,
                                 Map<String, Integer> teacherDayCount,
                                 int dayIndex, int slotIndex) {

        // Shuffle a copy so we try candidates in random order each time
        List<Subject> candidates = new ArrayList<>(pool);
        Collections.shuffle(candidates, random);

        for (Subject candidate : candidates) {
            // 1. Check weekly quota not exceeded
            int placed = weeklyCount.getOrDefault(candidate.getSubjectCode(), 0);
            if (placed >= candidate.getPeriodsPerWeek()) {
                continue;
            }

            // 2. Avoid same subject in consecutive slots on the same day
            if (timetable.isSameSubjectInPreviousSlot(dayIndex, slotIndex, candidate)) {
                continue;
            }

            // 3. Check teacher availability and workload
            Teacher teacher = subjectTeacherMap.get(candidate.getSubjectCode());
            if (teacher != null) {
                // Teacher conflict: already assigned in this slot on this day
                if (timetable.checkConflicts(dayIndex, slotIndex, teacher)) {
                    continue;
                }
                // Workload cap: teacher has too many periods today
                int teacherPeriods = teacherDayCount.getOrDefault(teacher.getTeacherId(), 0);
                if (teacherPeriods >= teacher.getMaxPeriodsPerDay()) {
                    continue;
                }
            }

            // 4. Avoid scheduling the same subject more than once per day
            if (timetable.countSubjectOnDay(dayIndex, candidate) >= 1) {
                continue;
            }

            return candidate; // valid candidate found
        }

        return null; // no valid candidate — leave cell FREE
    }

    /**
     * Picks a room from the available rooms list (round-robin).
     * Returns null if no rooms are configured.
     */
    private ClassRoom pickRoom() {
        if (rooms.isEmpty()) return null;
        return rooms.get(random.nextInt(rooms.size()));
    }

    /**
     * Builds a map from subjectCode → Teacher.
     * If multiple teachers can teach the same subject, the first one is used.
     * If no teacher is found for a subject, it maps to null.
     */
    private void buildSubjectTeacherMap() {
        for (Subject subject : subjects) {
            Teacher found = null;
            for (Teacher teacher : teachers) {
                if (teacher.teaches(subject)) {
                    found = teacher;
                    break;
                }
            }
            subjectTeacherMap.put(subject.getSubjectCode(), found);
        }
    }

    // ---------------------------------------------------------------
    // Workload Report
    // ---------------------------------------------------------------

    /**
     * Prints a summary of how many periods each teacher has been assigned.
     */
    public void printWorkloadReport(Timetable timetable) {
        System.out.println("\n========== TEACHER WORKLOAD REPORT ==========");
        for (Teacher teacher : teachers) {
            int total = 0;
            for (int d = 0; d < days.length; d++) {
                total += timetable.countTeacherPeriodsOnDay(d, teacher);
            }
            System.out.printf("  %-20s  Total Periods: %d%n",
                    teacher.getTeacherName(), total);
        }
        System.out.println("=============================================");
    }
}
