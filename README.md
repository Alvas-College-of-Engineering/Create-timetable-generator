# 📅 Timetable Generator

A complete, modular Java application that automatically generates a weekly class timetable for educational institutions. Available as both a **console app** and a **JSP web application**.

---

## ✨ Features

- 🔄 **Auto-generates** a full weekly timetable with zero manual scheduling
- 👨‍🏫 **Teacher conflict prevention** — no teacher is double-booked in the same time slot
- 📚 **Subject distribution** — subjects spread evenly across the week based on frequency
- 🚫 **No back-to-back repetition** — same subject won't appear in consecutive slots
- 🏫 **Classroom assignment** — rooms (including labs) assigned to each period
- 📊 **Workload tracking** — per-teacher period count report
- 💾 **Export support** — save timetable to `.txt` or `.csv` file
- 🌐 **Web interface** — JSP + Servlet based website with dark UI
- 🖥️ **Two run modes** — quick Demo mode or fully Interactive mode

---

## 🗂️ Project Structure

```
TimetableGenerator/
├── src/
│   ├── Main.java                              # Entry point — demo & interactive modes
│   ├── model/
│   │   ├── Subject.java                       # Subject entity (name, code, periods/week)
│   │   ├── Teacher.java                       # Teacher entity (name, ID, subjects, workload)
│   │   ├── TimeSlot.java                      # Time period (slot number, start/end time)
│   │   ├── ClassRoom.java                     # Room entity (number, capacity, lab flag)
│   │   └── ScheduleEntry.java                 # Single timetable cell (subject + teacher + room)
│   ├── timetable/
│   │   ├── Timetable.java                     # 2D grid, conflict checking, display logic
│   │   └── TimetableGenerator.java            # Core scheduling algorithm
│   └── util/
│       └── TimetableExporter.java             # Export to TXT and CSV
│
├── webapp/
│   ├── index.jsp                              # Home page — input form
│   ├── timetable.jsp                          # Result page — generated timetable
│   ├── error.jsp                              # Error page
│   └── WEB-INF/
│       ├── web.xml                            # Servlet configuration
│       └── src/servlet/
│           └── GenerateTimetableServlet.java  # Form handler servlet
│
├── pom.xml                                    # Maven build file (WAR packaging)
├── build.bat                                  # One-click build & run (console, Windows)
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Java JDK 11 or higher
- Maven (for web app)
- Apache Tomcat 10+ (for web app)

---

## 🖥️ Run Console App (No Setup Needed)

```bat
build.bat
```

Or manually:
```bash
mkdir out
javac -d out -sourcepath src src/Main.java
java -cp out Main
```

---

## 🌐 Run Web App

### Step 1 — Build WAR
```bash
mvn package
```

### Step 2 — Deploy to Tomcat
```bash
copy target\timetable-generator-1.0.war C:\tomcat\webapps\
C:\tomcat\bin\startup.bat
```

### Step 3 — Open in Browser
```
http://localhost:8080/timetable-generator-1.0
```

> **Easiest way:** Open in IntelliJ IDEA → Add Tomcat server → Click Run ▶️

---

## 🖥️ Console Output Sample

```
╔══════════════════════════════════════════════╗
║        TIMETABLE GENERATOR  v1.0             ║
║   Automated Weekly Class Schedule System     ║
╚══════════════════════════════════════════════╝

========== WEEKLY TIMETABLE (COMPACT) ==========

Time        | Monday       | Tuesday      | Wednesday    | Thursday     | Friday       |
------------|--------------|--------------|--------------|--------------|--------------|
08:00-09:00 | PHY/Bob      | HIST/Eva     | PHY/Bob      | MATH/Alice   | MATH/Alice   |
09:00-10:00 | MATH/Alice   | MATH/Alice   | MATH/Alice   | ENG/Carol    |   FREE       |
10:00-11:00 | PE/Eva       | CHEM/Bob     | CHEM/Bob     | PHY/Bob      |   FREE       |
11:00-12:00 | CS/David     | PHY/Bob      | ENG/Carol    | CS/David     |   FREE       |
13:00-14:00 | ENG/Carol    | CS/David     | HIST/Eva     |   FREE       |   FREE       |
14:00-15:00 | CHEM/Bob     | PE/Eva       | CS/David     |   FREE       |   FREE       |
15:00-16:00 | HIST/Eva     | ENG/Carol    |   FREE       |   FREE       |   FREE       |

========== TEACHER WORKLOAD REPORT ==========
  Alice Johnson         Total Periods: 5
  Bob Smith             Total Periods: 7
  Carol White           Total Periods: 4
  David Lee             Total Periods: 4
  Eva Brown             Total Periods: 5
=============================================
```

---

## 🌐 Web App Pages

| Page | URL | Description |
|---|---|---|
| Home | `/index.jsp` | Input form — add subjects, teachers, rooms |
| Result | `/timetable.jsp` | Generated timetable with stats & workload |
| Error | `/error.jsp` | Friendly error page |

### Web App Features
- ⚡ **Load Demo** button — auto-fills sample data instantly
- ➕ **Dynamic rows** — add/remove subjects and teachers on the fly
- 📊 **Stats cards** — total subjects, teachers, filled/free slots
- 📈 **Workload bars** — visual teacher workload indicator
- 🖨️ **Print button** — print-ready timetable layout

---

## ⚙️ How the Algorithm Works

1. **Weighted pool** — each subject added proportional to its `periodsPerWeek`, then shuffled
2. **Grid iteration** — loops through every `[day][slot]` cell
3. **Candidate selection** — picks the first subject passing all rules:
   - ✅ Weekly quota not yet reached
   - ✅ Not same subject as previous slot (no back-to-back)
   - ✅ Teacher not already assigned in this slot today
   - ✅ Teacher hasn't hit their daily period cap
   - ✅ Subject not already placed on this day
4. **Assignment** — valid entries stored in `ScheduleEntry[][]` matrix
5. **FREE cells** — if no candidate passes all rules, slot stays empty

> The timetable is **randomized on every run** — each execution produces a different but equally valid schedule.

---

## 🏗️ Class Design

| Class | Package | Responsibility |
|---|---|---|
| `Subject` | `model` | Subject name, code, weekly frequency |
| `Teacher` | `model` | Teacher info, assigned subjects, workload cap |
| `TimeSlot` | `model` | Single time period with start/end times |
| `ClassRoom` | `model` | Room or lab with capacity info |
| `ScheduleEntry` | `model` | Timetable cell — subject + teacher + room |
| `Timetable` | `timetable` | 2D schedule grid with conflict checking and display |
| `TimetableGenerator` | `timetable` | Core algorithm that fills the timetable |
| `TimetableExporter` | `util` | Exports timetable to `.txt` or `.csv` |
| `GenerateTimetableServlet` | `servlet` | Handles web form POST, runs generator, forwards to JSP |
| `Main` | — | Entry point for console app |

---

## 🧰 OOP Concepts Used

- **Encapsulation** — all fields private with getters/setters
- **Constructors** — default and parameterized in every class
- **ArrayList & Arrays** — subjects, teachers, rooms, schedule grid
- **Modular design** — each class has a single clear responsibility
- **Error handling** — invalid inputs throw `IllegalArgumentException`
- **MVC pattern** — Servlet (Controller) + JSP (View) + model classes (Model)

---

## 📤 Export (Console App)

At the end of each run you can export:
- **TXT** → `timetable.txt` (formatted table)
- **CSV** → `timetable.csv` (importable into Excel / Google Sheets)

---

## 📄 License

This project is open source and free to use for educational purposes.
