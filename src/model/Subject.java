package model;

/**
 * Represents an academic subject in the timetable system.
 * Encapsulates subject name, code, and scheduling frequency.
 */
public class Subject {

    private String subjectName;
    private String subjectCode;
    private int periodsPerWeek; // how many times this subject appears per week

    // Default constructor
    public Subject() {
        this.subjectName = "Unknown";
        this.subjectCode = "N/A";
        this.periodsPerWeek = 3;
    }

    // Parameterized constructor
    public Subject(String subjectName, String subjectCode, int periodsPerWeek) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.periodsPerWeek = periodsPerWeek;
    }

    // Getters
    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public int getPeriodsPerWeek() {
        return periodsPerWeek;
    }

    // Setters
    public void setSubjectName(String subjectName) {
        if (subjectName == null || subjectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be null or empty.");
        }
        this.subjectName = subjectName;
    }

    public void setSubjectCode(String subjectCode) {
        if (subjectCode == null || subjectCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject code cannot be null or empty.");
        }
        this.subjectCode = subjectCode;
    }

    public void setPeriodsPerWeek(int periodsPerWeek) {
        if (periodsPerWeek <= 0) {
            throw new IllegalArgumentException("Periods per week must be a positive number.");
        }
        this.periodsPerWeek = periodsPerWeek;
    }

    @Override
    public String toString() {
        return subjectName + " (" + subjectCode + ")";
    }
}
