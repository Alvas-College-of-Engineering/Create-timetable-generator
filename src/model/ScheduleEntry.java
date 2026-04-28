package model;

/**
 * Represents a single cell in the timetable grid.
 * Holds the subject, teacher, and room assigned to a specific day + time slot.
 */
public class ScheduleEntry {

    private Subject subject;
    private Teacher teacher;
    private ClassRoom room;

    // Constructor for a filled entry
    public ScheduleEntry(Subject subject, Teacher teacher, ClassRoom room) {
        this.subject = subject;
        this.teacher = teacher;
        this.room = room;
    }

    // Constructor without room (room is optional)
    public ScheduleEntry(Subject subject, Teacher teacher) {
        this(subject, teacher, null);
    }

    // Getters
    public Subject getSubject() {
        return subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public ClassRoom getRoom() {
        return room;
    }

    /**
     * Returns a compact display string for the timetable cell.
     * Format: SubjectCode / TeacherInitials
     */
    public String toDisplayString() {
        if (subject == null) return "  FREE  ";
        String teacherShort = teacher != null
                ? teacher.getTeacherName().split(" ")[0] // first name only
                : "---";
        return subject.getSubjectCode() + "/" + teacherShort;
    }

    @Override
    public String toString() {
        return "ScheduleEntry{subject=" + subject + ", teacher=" + teacher + ", room=" + room + "}";
    }
}
