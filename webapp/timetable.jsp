<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="timetable.Timetable" %>
<%@ page import="model.ScheduleEntry, model.TimeSlot, model.Teacher, model.Subject" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Generated Timetable</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
            min-height: 100vh;
            color: #eee;
        }

        header {
            background: rgba(255,255,255,0.05);
            backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(255,255,255,0.1);
            padding: 20px 40px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 15px;
        }

        .header-left { display: flex; align-items: center; gap: 15px; }
        .logo { font-size: 2rem; }

        header h1 {
            font-size: 1.6rem;
            font-weight: 700;
            background: linear-gradient(90deg, #e94560, #0f3460);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 10px 22px;
            border: none;
            border-radius: 50px;
            font-size: 0.9rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
        }

        .btn-back {
            background: rgba(255,255,255,0.1);
            color: #fff;
            border: 1px solid rgba(255,255,255,0.2);
        }

        .btn-back:hover { background: rgba(255,255,255,0.2); }

        .btn-print {
            background: linear-gradient(135deg, #e94560, #c62a47);
            color: #fff;
            box-shadow: 0 4px 15px rgba(233,69,96,0.4);
        }

        .btn-print:hover { transform: translateY(-2px); }

        .container { max-width: 1200px; margin: 0 auto; padding: 40px 20px; }

        .page-title {
            text-align: center;
            margin-bottom: 40px;
        }

        .page-title h2 { font-size: 2rem; color: #fff; margin-bottom: 8px; }
        .page-title p { color: #aaa; font-size: 0.95rem; }

        /* Timetable Table */
        .table-wrapper {
            overflow-x: auto;
            border-radius: 16px;
            border: 1px solid rgba(255,255,255,0.1);
            margin-bottom: 40px;
            box-shadow: 0 8px 32px rgba(0,0,0,0.3);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 700px;
        }

        thead th {
            background: linear-gradient(135deg, #e94560, #c62a47);
            color: #fff;
            padding: 16px 12px;
            font-size: 0.95rem;
            font-weight: 600;
            text-align: center;
            white-space: nowrap;
        }

        thead th:first-child {
            border-radius: 16px 0 0 0;
            text-align: left;
            padding-left: 20px;
        }

        thead th:last-child { border-radius: 0 16px 0 0; }

        tbody tr:nth-child(even) { background: rgba(255,255,255,0.03); }
        tbody tr:nth-child(odd)  { background: rgba(255,255,255,0.06); }
        tbody tr:hover           { background: rgba(233,69,96,0.08); }

        tbody td {
            padding: 14px 10px;
            text-align: center;
            border-bottom: 1px solid rgba(255,255,255,0.05);
            font-size: 0.88rem;
            vertical-align: middle;
        }

        tbody td:first-child {
            text-align: left;
            padding-left: 20px;
            font-weight: 600;
            color: #e94560;
            white-space: nowrap;
        }

        .cell-filled {
            background: rgba(233,69,96,0.12);
            border: 1px solid rgba(233,69,96,0.25);
            border-radius: 10px;
            padding: 8px 6px;
            display: inline-block;
            min-width: 90px;
        }

        .cell-subject {
            font-weight: 700;
            color: #fff;
            font-size: 0.85rem;
        }

        .cell-teacher {
            font-size: 0.75rem;
            color: #aaa;
            margin-top: 3px;
        }

        .cell-room {
            font-size: 0.7rem;
            color: #666;
            margin-top: 2px;
        }

        .cell-free {
            color: #444;
            font-size: 0.8rem;
            font-style: italic;
        }

        /* Stats row */
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }

        .stat-card {
            background: rgba(255,255,255,0.05);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 14px;
            padding: 20px;
            text-align: center;
        }

        .stat-card .num {
            font-size: 2rem;
            font-weight: 700;
            color: #e94560;
        }

        .stat-card .label {
            font-size: 0.85rem;
            color: #aaa;
            margin-top: 5px;
        }

        /* Workload table */
        .section-title {
            font-size: 1.3rem;
            color: #e94560;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .workload-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
            gap: 15px;
            margin-bottom: 40px;
        }

        .workload-card {
            background: rgba(255,255,255,0.05);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 14px;
            padding: 18px 20px;
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .workload-avatar {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            background: linear-gradient(135deg, #e94560, #c62a47);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.2rem;
            font-weight: 700;
            color: #fff;
            flex-shrink: 0;
        }

        .workload-info .name { font-weight: 600; color: #fff; font-size: 0.95rem; }
        .workload-info .periods { font-size: 0.82rem; color: #aaa; margin-top: 3px; }

        .workload-bar-wrap {
            flex: 1;
            background: rgba(255,255,255,0.08);
            border-radius: 10px;
            height: 6px;
            overflow: hidden;
        }

        .workload-bar {
            height: 100%;
            background: linear-gradient(90deg, #e94560, #ff6b6b);
            border-radius: 10px;
            transition: width 0.5s ease;
        }

        /* Subject legend */
        .legend {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-bottom: 40px;
        }

        .legend-item {
            background: rgba(255,255,255,0.05);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 20px;
            padding: 6px 14px;
            font-size: 0.82rem;
            color: #ccc;
        }

        .legend-item span {
            font-weight: 700;
            color: #e94560;
            margin-right: 5px;
        }

        footer {
            text-align: center;
            padding: 30px;
            color: #555;
            font-size: 0.85rem;
            border-top: 1px solid rgba(255,255,255,0.05);
        }

        @media print {
            body { background: #fff; color: #000; }
            header, .btn, footer, .stats, .workload-grid, .legend { display: none; }
            .table-wrapper { border: 1px solid #ccc; box-shadow: none; }
            thead th { background: #e94560 !important; -webkit-print-color-adjust: exact; }
        }
    </style>
</head>
<body>

<%
    Timetable timetable   = (Timetable) request.getAttribute("timetable");
    String[]  days        = (String[])  request.getAttribute("days");
    TimeSlot[] slots      = (TimeSlot[]) request.getAttribute("slots");
    List<Teacher> teachers = (List<Teacher>) request.getAttribute("teachers");
    List<Subject> subjects = (List<Subject>) request.getAttribute("subjects");
    int[] workload         = (int[]) request.getAttribute("workload");

    // Count total assigned periods
    int totalAssigned = 0;
    int totalCells    = days.length * slots.length;
    for (int d = 0; d < days.length; d++) {
        for (int s = 0; s < slots.length; s++) {
            if (timetable.getEntry(d, s) != null) totalAssigned++;
        }
    }
    int maxWork = 0;
    for (int w : workload) if (w > maxWork) maxWork = w;
%>

<header>
    <div class="header-left">
        <div class="logo">📅</div>
        <div>
            <h1>Timetable Generator</h1>
        </div>
    </div>
    <div style="display:flex;gap:10px;flex-wrap:wrap;">
        <a href="index.jsp" class="btn btn-back">← New Timetable</a>
        <button onclick="window.print()" class="btn btn-print">🖨️ Print</button>
    </div>
</header>

<div class="container">

    <div class="page-title">
        <h2>✅ Timetable Generated Successfully</h2>
        <p>Weekly schedule for <%= days.length %> days × <%= slots.length %> periods</p>
    </div>

    <!-- Stats -->
    <div class="stats">
        <div class="stat-card">
            <div class="num"><%= subjects.size() %></div>
            <div class="label">Subjects</div>
        </div>
        <div class="stat-card">
            <div class="num"><%= teachers.size() %></div>
            <div class="label">Teachers</div>
        </div>
        <div class="stat-card">
            <div class="num"><%= days.length %></div>
            <div class="label">Days / Week</div>
        </div>
        <div class="stat-card">
            <div class="num"><%= slots.length %></div>
            <div class="label">Periods / Day</div>
        </div>
        <div class="stat-card">
            <div class="num"><%= totalAssigned %></div>
            <div class="label">Slots Filled</div>
        </div>
        <div class="stat-card">
            <div class="num"><%= totalCells - totalAssigned %></div>
            <div class="label">Free Slots</div>
        </div>
    </div>

    <!-- Subject Legend -->
    <div class="section-title">📚 Subject Legend</div>
    <div class="legend">
        <% for (Subject s : subjects) { %>
        <div class="legend-item">
            <span><%= s.getSubjectCode() %></span><%= s.getSubjectName() %> &nbsp;·&nbsp; <%= s.getPeriodsPerWeek() %>/week
        </div>
        <% } %>
    </div>

    <!-- Timetable -->
    <div class="section-title">📋 Weekly Timetable</div>
    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Time</th>
                    <% for (String day : days) { %>
                    <th><%= day %></th>
                    <% } %>
                </tr>
            </thead>
            <tbody>
                <% for (int s = 0; s < slots.length; s++) { %>
                <tr>
                    <td><%= slots[s].display() %></td>
                    <% for (int d = 0; d < days.length; d++) {
                        ScheduleEntry entry = timetable.getEntry(d, s);
                        if (entry != null && entry.getSubject() != null) {
                            String teacherName = entry.getTeacher() != null
                                ? entry.getTeacher().getTeacherName().split(" ")[0]
                                : "";
                            String roomName = entry.getRoom() != null
                                ? entry.getRoom().getRoomNumber()
                                : "";
                    %>
                    <td>
                        <div class="cell-filled">
                            <div class="cell-subject"><%= entry.getSubject().getSubjectCode() %></div>
                            <div class="cell-teacher"><%= teacherName %></div>
                            <% if (!roomName.isEmpty()) { %>
                            <div class="cell-room"><%= roomName %></div>
                            <% } %>
                        </div>
                    </td>
                    <% } else { %>
                    <td><span class="cell-free">FREE</span></td>
                    <% } } %>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <!-- Teacher Workload -->
    <div class="section-title">👨‍🏫 Teacher Workload</div>
    <div class="workload-grid">
        <% for (int t = 0; t < teachers.size(); t++) {
            Teacher teacher = teachers.get(t);
            int periods = workload[t];
            int barPct  = maxWork > 0 ? (periods * 100 / maxWork) : 0;
            String initial = teacher.getTeacherName().substring(0, 1).toUpperCase();
        %>
        <div class="workload-card">
            <div class="workload-avatar"><%= initial %></div>
            <div class="workload-info" style="flex:1">
                <div class="name"><%= teacher.getTeacherName() %></div>
                <div class="periods"><%= periods %> periods this week</div>
                <div class="workload-bar-wrap" style="margin-top:8px">
                    <div class="workload-bar" style="width:<%= barPct %>%"></div>
                </div>
            </div>
        </div>
        <% } %>
    </div>

</div>

<footer>
    <p>Timetable Generator v1.0 &nbsp;|&nbsp; Built with Java Servlets &amp; JSP</p>
</footer>

</body>
</html>
