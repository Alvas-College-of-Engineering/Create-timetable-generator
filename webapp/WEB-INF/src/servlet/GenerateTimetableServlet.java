package servlet;

import model.ClassRoom;
import model.ScheduleEntry;
import model.Subject;
import model.Teacher;
import model.TimeSlot;
import timetable.Timetable;
import timetable.TimetableGenerator;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles POST /generate
 * Reads form data, builds model objects, runs TimetableGenerator,
 * then forwards the result to timetable.jsp for display.
 */
public class GenerateTimetableServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // ── 1. Parse Subjects ──────────────────────────────────────
            String[] subjectNames   = request.getParameterValues("subjectName");
            String[] subjectCodes   = request.getParameterValues("subjectCode");
            String[] subjectPeriods = request.getParameterValues("subjectPeriods");

            List<Subject> subjects = new ArrayList<>();
            if (subjectNames != null) {
                for (int i = 0; i < subjectNames.length; i++) {
                    String name = subjectNames[i].trim();
                    String code = subjectCodes[i].trim().toUpperCase();
                    int periods = Integer.parseInt(subjectPeriods[i].trim());
                    if (!name.isEmpty() && !code.isEmpty()) {
                        subjects.add(new Subject(name, code, periods));
                    }
                }
            }

            if (subjects.isEmpty()) {
                request.setAttribute("error", "Please add at least one subject.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }

            // ── 2. Parse Teachers ──────────────────────────────────────
            String[] teacherNames      = request.getParameterValues("teacherName");
            String[] teacherIds        = request.getParameterValues("teacherId");
            String[] teacherSubjects   = request.getParameterValues("teacherSubjects");
            String[] teacherMaxPeriods = request.getParameterValues("teacherMaxPeriods");

            List<Teacher> teachers = new ArrayList<>();
            if (teacherNames != null) {
                for (int i = 0; i < teacherNames.length; i++) {
                    String name = teacherNames[i].trim();
                    String id   = teacherIds[i].trim();
                    int maxP    = Integer.parseInt(teacherMaxPeriods[i].trim());

                    Teacher teacher = new Teacher(name, id, maxP);

                    // Assign subjects by matching codes
                    String[] codes = teacherSubjects[i].split(",");
                    for (String code : codes) {
                        String c = code.trim().toUpperCase();
                        for (Subject s : subjects) {
                            if (s.getSubjectCode().equals(c)) {
                                teacher.assignSubject(s);
                            }
                        }
                    }
                    teachers.add(teacher);
                }
            }

            // ── 3. Parse Rooms ─────────────────────────────────────────
            String roomsParam = request.getParameter("rooms");
            List<ClassRoom> rooms = new ArrayList<>();
            if (roomsParam != null && !roomsParam.trim().isEmpty()) {
                String[] roomArr = roomsParam.split(",");
                for (String r : roomArr) {
                    String rn = r.trim();
                    if (!rn.isEmpty()) {
                        boolean isLab = rn.toUpperCase().contains("LAB");
                        rooms.add(new ClassRoom(rn, 40, isLab));
                    }
                }
            }

            // ── 4. Build Days ──────────────────────────────────────────
            String includeSat = request.getParameter("includeSaturday");
            String[] days = "yes".equals(includeSat)
                    ? new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"}
                    : new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday"};

            // ── 5. Build Time Slots ────────────────────────────────────
            int periodsPerDay = Integer.parseInt(request.getParameter("periodsPerDay"));
            TimeSlot[] slots = buildTimeSlots(periodsPerDay);

            // ── 6. Generate Timetable ──────────────────────────────────
            TimetableGenerator generator = new TimetableGenerator(subjects, teachers, rooms, days, slots);
            Timetable timetable = generator.generateTimetable();

            // ── 7. Build workload data for display ─────────────────────
            int[] workload = new int[teachers.size()];
            for (int t = 0; t < teachers.size(); t++) {
                for (int d = 0; d < days.length; d++) {
                    workload[t] += timetable.countTeacherPeriodsOnDay(d, teachers.get(t));
                }
            }

            // ── 8. Forward to result JSP ───────────────────────────────
            request.setAttribute("timetable",  timetable);
            request.setAttribute("subjects",   subjects);
            request.setAttribute("teachers",   teachers);
            request.setAttribute("workload",   workload);
            request.setAttribute("days",       days);
            request.setAttribute("slots",      slots);

            request.getRequestDispatcher("timetable.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number entered: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error generating timetable: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    /**
     * Builds time slots starting at 08:00, 1 hour each,
     * with a lunch break inserted after slot 4 (skips 12:00–13:00).
     */
    private TimeSlot[] buildTimeSlots(int count) {
        TimeSlot[] slots = new TimeSlot[count];
        int hour = 8;
        for (int i = 0; i < count; i++) {
            if (hour == 12) hour = 13; // lunch break
            slots[i] = new TimeSlot(i + 1,
                    String.format("%02d:00", hour),
                    String.format("%02d:00", hour + 1));
            hour++;
        }
        return slots;
    }
}
