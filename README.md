# 📅 Timetable Generator

A complete, modular Java application that automatically generates a weekly class timetable for educational institutions. Built with clean OOP principles — no frameworks, no dependencies, just pure Java.

---

## ✨ Features

- 🔄 **Auto-generates** a full weekly timetable with zero manual scheduling
- 👨‍🏫 **Teacher conflict prevention** — no teacher is double-booked in the same time slot
- 📚 **Subject distribution** — subjects spread evenly across the week based on frequency
- 🚫 **No back-to-back repetition** — same subject won't appear in consecutive slots
- 🏫 **Classroom assignment** — rooms (including labs) assigned to each period
- 📊 **Workload tracking** — per-teacher period count report
- 💾 **Export support** — save timetable to `.txt` or `.csv` file
- 🖥️ **Two run modes** — quick Demo mode or fully Interactive mode

---

## 🗂️ Project Structure

```
TimetableGenerator/
├── src/
│   ├── Main.java                    # Entry point — demo & interactive modes
│   ├── model/
│   │   ├── Subject.java             # Subject entity (name, code, periods/week)
│   │   ├── Teacher.java             # Teacher entity (name, ID, subjects, workload)
│   │   ├── TimeSlot.java            # Time period (slot number, start/end time)
│   │   ├── ClassRoom.java           # Room entity (number, capacity, lab flag)
│   │   └── ScheduleEntry.java       # Single timetable cell (subject + teacher + room)
│   ├── timetable/
│   │   ├── Timetable.java           # 2D grid, conflict checking, display logic
│   │   └── TimetableGenerator.java  # Core scheduling algorithm
│   └── util/
│       └── TimetableExporter.java   # Export to TXT and CSV
├── build.bat                        # One-click build & run script (Windows)
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- Java JDK 8 or higher
- Command Prompt / Terminal

### Compile & Run (Windows)

```bat
build.bat
```

### Compile & Run (Manual)

```bash
# Compile
mkdir out
javac -d out -sourcepath src src/Main.java

# Run
java -cp out Main
```

---

## 🖥️ Usage

On launch, choose a mode:

```
╔══════════════════════════════════════════════╗
║        TIMETABLE GENERATOR  v1.0             ║
║   Automated Weekly Class Schedule System     ║
╚══════════════════════════════════════════════╝
Choose mode:
  1. Demo mode  (pre-configured sample data)
  2. Interactive mode (enter your own data)
Enter choice [1/2]:
```

### Mode 1 — Demo
Instantly generates a timetable using built-in sample data:
- **7 subjects:** Mathematics, Physics, Chemistry, English, Computer Science, History, PE
- **5 teachers:** Alice Johnson, Bob Smith, Carol White, David Lee, Eva Brown
- **4 rooms:** R101, R102, R103, LAB1
- **5 days:** Monday–Friday, 7 periods per day (08:00–16:00)

### Mode 2 — Interactive
Enter your own subjects, teachers, classrooms, and schedule preferences step by step.

---

## 📋 Sample Output

```
Time        | Monday       | Tuesday      | Wednesday    | Thursday     | Friday       |
------------|--------------|--------------|--------------|--------------|--------------|
08:00-09:00 | PHY/Bob      | HIST/Eva     | PHY/Bob      | MATH/Alice   | MATH/Alice   |
09:00-10:00 | MATH/Alice   | MATH/Alice   | MATH/Alice   | ENG/Carol    |   FREE       |
10:00-11:00 | PE/Eva       | CHEM/Bob     | CHEM/Bob     | PHY/Bob      |   FREE       |
11:00-12:00 | CS/David     | PHY/Bob      | ENG/Carol    | CS/David     |   FREE       |
13:00-14:00 | ENG/Carol    | CS/David     | HIST/Eva     |   FREE       |   FREE       |
14:00-15:00 | CHEM/Bob     | PE/Eva       | CS/David     |   FREE       |   FREE       |
15:00-16:00 | HIST/Eva     | ENG/Carol    |   FREE       |   FREE       |   FREE       |
```

### Teacher Workload Report

```
========== TEACHER WORKLOAD REPORT ==========
  Alice Johnson         Total Periods: 5
  Bob Smith             Total Periods: 7
  Carol White           Total Periods: 4
  David Lee             Total Periods: 4
  Eva Brown             Total Periods: 5
=============================================
```

---

## ⚙️ How the Algorithm Works

1. **Weighted pool** — each subject is added to a list proportional to its `periodsPerWeek` value, then shuffled randomly
2. **Grid iteration** — loops through every `[day][slot]` cell in the timetable
3. **Candidate selection** — for each cell, picks the first subject from the shuffled pool that satisfies all rules:
   - ✅ Weekly quota not yet reached
   - ✅ Not the same subject as the previous slot (no back-to-back)
   - ✅ Teacher not already assigned in this slot today
   - ✅ Teacher hasn't hit their daily period cap
   - ✅ Subject not already placed on this day
4. **Assignment** — valid entries stored in the `ScheduleEntry[][]` matrix
5. **FREE cells** — if no candidate passes all rules, the slot stays empty

The timetable is **randomized on every run**, so each execution produces a different but equally valid schedule.

---

## 🏗️ Class Design

| Class | Package | Responsibility |
|---|---|---|
| `Subject` | `model` | Stores subject name, code, and weekly frequency |
| `Teacher` | `model` | Stores teacher info, assigned subjects, and workload cap |
| `TimeSlot` | `model` | Represents a single time period with start/end times |
| `ClassRoom` | `model` | Represents a room or lab with capacity info |
| `ScheduleEntry` | `model` | A single timetable cell — subject + teacher + room |
| `Timetable` | `timetable` | 2D schedule grid with conflict checking and display |
| `TimetableGenerator` | `timetable` | Core algorithm that fills the timetable |
| `TimetableExporter` | `util` | Exports timetable to `.txt` or `.csv` |
| `Main` | — | Entry point, handles demo and interactive modes |

---

## 📤 Export

At the end of each run, you can export the timetable:

- **TXT** → `timetable.txt` (formatted table, same as console output)
- **CSV** → `timetable.csv` (importable into Excel / Google Sheets)

---

## 🧰 OOP Concepts Used

- **Encapsulation** — all fields are private with getters/setters
- **Constructors** — both default and parameterized constructors in every class
- **ArrayList & Arrays** — used for subjects, teachers, rooms, and the schedule grid
- **Modular design** — each class has a single, clear responsibility
- **Error handling** — invalid inputs throw `IllegalArgumentException` with descriptive messages

---

## 📄 License

This project is open source and free to use for educational purposes.
