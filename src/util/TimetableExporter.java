package util;

import model.ScheduleEntry;
import timetable.Timetable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class for exporting the timetable to external files.
 * Supports plain-text (.txt) and comma-separated (.csv) formats.
 */
public class TimetableExporter {

    private TimetableExporter() {
        // Utility class — no instantiation needed
    }

    // ---------------------------------------------------------------
    // TXT Export
    // ---------------------------------------------------------------

    /**
     * Exports the timetable to a formatted .txt file.
     *
     * @param timetable the timetable to export
     * @param filePath  destination file path (e.g., "timetable.txt")
     */
    public static void exportToTxt(Timetable timetable, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            String[] days = timetable.getDays();
            int colWidth = 14;
            int timeColWidth = 12;
            int totalWidth = timeColWidth + (colWidth * days.length) + days.length + 1;
            String separator = "-".repeat(totalWidth);

            writer.write(separator);
            writer.newLine();

            // Header row
            writer.write(String.format("%-" + timeColWidth + "s", "Time"));
            for (String day : days) {
                writer.write(String.format("| %-" + (colWidth - 1) + "s", day));
            }
            writer.write("|");
            writer.newLine();
            writer.write(separator);
            writer.newLine();

            // Data rows
            for (int s = 0; s < timetable.getTimeSlots().length; s++) {
                writer.write(String.format("%-" + timeColWidth + "s",
                        timetable.getTimeSlots()[s].display()));

                for (int d = 0; d < days.length; d++) {
                    ScheduleEntry entry = timetable.getEntry(d, s);
                    String cell = (entry != null) ? entry.toDisplayString() : "  FREE  ";
                    writer.write(String.format("| %-" + (colWidth - 1) + "s", cell));
                }
                writer.write("|");
                writer.newLine();
            }
            writer.write(separator);
            writer.newLine();

            System.out.println("[Export] Timetable saved to: " + filePath);

        } catch (IOException e) {
            System.err.println("[Export Error] Could not write TXT file: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // CSV Export
    // ---------------------------------------------------------------

    /**
     * Exports the timetable to a .csv file.
     * Each row is a time slot; each column is a day.
     *
     * @param timetable the timetable to export
     * @param filePath  destination file path (e.g., "timetable.csv")
     */
    public static void exportToCsv(Timetable timetable, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            String[] days = timetable.getDays();

            // Header row
            writer.write("Time");
            for (String day : days) {
                writer.write("," + day);
            }
            writer.newLine();

            // Data rows
            for (int s = 0; s < timetable.getTimeSlots().length; s++) {
                writer.write(timetable.getTimeSlots()[s].display());

                for (int d = 0; d < days.length; d++) {
                    ScheduleEntry entry = timetable.getEntry(d, s);
                    String cell = (entry != null) ? entry.toDisplayString() : "FREE";
                    // Wrap in quotes to handle commas in names
                    writer.write(",\"" + cell + "\"");
                }
                writer.newLine();
            }

            System.out.println("[Export] Timetable saved to: " + filePath);

        } catch (IOException e) {
            System.err.println("[Export Error] Could not write CSV file: " + e.getMessage());
        }
    }
}
