package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a teacher who is assigned to one or more subjects.
 * Tracks workload and availability to prevent scheduling conflicts.
 */
public class Teacher {

    private String teacherName;
    private String teacherId;
    private List<Subject> subjectsAssigned; // a teacher can handle multiple subjects
    private int maxPeriodsPerDay;           // workload cap per day

    // Default constructor
    public Teacher() {
        this.teacherName = "Unknown";
        this.teacherId = "T000";
        this.subjectsAssigned = new ArrayList<>();
        this.maxPeriodsPerDay = 4;
    }

    // Parameterized constructor
    public Teacher(String teacherName, String teacherId, int maxPeriodsPerDay) {
        this.teacherName = teacherName;
        this.teacherId = teacherId;
        this.subjectsAssigned = new ArrayList<>();
        this.maxPeriodsPerDay = maxPeriodsPerDay;
    }

    // Assign a subject to this teacher
    public void assignSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Cannot assign a null subject.");
        }
        if (!subjectsAssigned.contains(subject)) {
            subjectsAssigned.add(subject);
        }
    }

    // Check if this teacher teaches a given subject
    public boolean teaches(Subject subject) {
        return subjectsAssigned.contains(subject);
    }

    // Getters
    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public List<Subject> getSubjectsAssigned() {
        return subjectsAssigned;
    }

    public int getMaxPeriodsPerDay() {
        return maxPeriodsPerDay;
    }

    // Setters
    public void setTeacherName(String teacherName) {
        if (teacherName == null || teacherName.trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher name cannot be null or empty.");
        }
        this.teacherName = teacherName;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public void setMaxPeriodsPerDay(int maxPeriodsPerDay) {
        if (maxPeriodsPerDay <= 0) {
            throw new IllegalArgumentException("Max periods per day must be positive.");
        }
        this.maxPeriodsPerDay = maxPeriodsPerDay;
    }

    @Override
    public String toString() {
        return teacherName + " [" + teacherId + "]";
    }
}
