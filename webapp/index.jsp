<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Timetable Generator</title>
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
            gap: 15px;
        }

        header .logo { font-size: 2rem; }

        header h1 {
            font-size: 1.8rem;
            font-weight: 700;
            background: linear-gradient(90deg, #e94560, #0f3460);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        header p { color: #aaa; font-size: 0.9rem; margin-top: 2px; }

        .container { max-width: 1100px; margin: 0 auto; padding: 40px 20px; }

        .hero {
            text-align: center;
            padding: 60px 20px 40px;
        }

        .hero h2 {
            font-size: 2.5rem;
            margin-bottom: 15px;
            color: #fff;
        }

        .hero p {
            font-size: 1.1rem;
            color: #aaa;
            max-width: 600px;
            margin: 0 auto 40px;
            line-height: 1.7;
        }

        .cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 20px;
            margin-bottom: 50px;
        }

        .card {
            background: rgba(255,255,255,0.05);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 16px;
            padding: 25px;
            text-align: center;
            transition: transform 0.2s, border-color 0.2s;
        }

        .card:hover {
            transform: translateY(-5px);
            border-color: #e94560;
        }

        .card .icon { font-size: 2.5rem; margin-bottom: 12px; }
        .card h3 { font-size: 1rem; color: #fff; margin-bottom: 8px; }
        .card p { font-size: 0.85rem; color: #aaa; line-height: 1.5; }

        /* Form */
        .form-section {
            background: rgba(255,255,255,0.05);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 20px;
            padding: 40px;
            margin-bottom: 40px;
        }

        .form-section h2 {
            font-size: 1.5rem;
            margin-bottom: 30px;
            color: #e94560;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .form-group.full { grid-column: 1 / -1; }

        label {
            font-size: 0.9rem;
            color: #ccc;
            font-weight: 500;
        }

        input, select, textarea {
            background: rgba(255,255,255,0.08);
            border: 1px solid rgba(255,255,255,0.15);
            border-radius: 10px;
            padding: 12px 15px;
            color: #fff;
            font-size: 0.95rem;
            outline: none;
            transition: border-color 0.2s;
            width: 100%;
        }

        input:focus, select:focus, textarea:focus {
            border-color: #e94560;
        }

        textarea { resize: vertical; min-height: 100px; }

        select option { background: #1a1a2e; }

        .btn {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            padding: 14px 35px;
            border: none;
            border-radius: 50px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
        }

        .btn-primary {
            background: linear-gradient(135deg, #e94560, #c62a47);
            color: #fff;
            box-shadow: 0 4px 20px rgba(233,69,96,0.4);
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 25px rgba(233,69,96,0.6);
        }

        .btn-demo {
            background: rgba(255,255,255,0.1);
            color: #fff;
            border: 1px solid rgba(255,255,255,0.2);
        }

        .btn-demo:hover {
            background: rgba(255,255,255,0.2);
            transform: translateY(-2px);
        }

        .btn-group {
            display: flex;
            gap: 15px;
            margin-top: 30px;
            flex-wrap: wrap;
        }

        .divider {
            display: flex;
            align-items: center;
            gap: 15px;
            margin: 30px 0;
            color: #666;
        }

        .divider::before, .divider::after {
            content: '';
            flex: 1;
            height: 1px;
            background: rgba(255,255,255,0.1);
        }

        /* Subject / Teacher dynamic rows */
        .dynamic-list { display: flex; flex-direction: column; gap: 10px; }

        .dynamic-row {
            display: grid;
            grid-template-columns: 1fr 1fr 80px 40px;
            gap: 10px;
            align-items: center;
        }

        .remove-btn {
            background: rgba(233,69,96,0.2);
            border: 1px solid #e94560;
            color: #e94560;
            border-radius: 8px;
            padding: 8px;
            cursor: pointer;
            font-size: 1rem;
            transition: background 0.2s;
        }

        .remove-btn:hover { background: rgba(233,69,96,0.4); }

        .add-btn {
            background: rgba(255,255,255,0.05);
            border: 1px dashed rgba(255,255,255,0.3);
            color: #aaa;
            border-radius: 10px;
            padding: 10px;
            cursor: pointer;
            font-size: 0.9rem;
            transition: all 0.2s;
            width: 100%;
            margin-top: 5px;
        }

        .add-btn:hover {
            border-color: #e94560;
            color: #e94560;
        }

        footer {
            text-align: center;
            padding: 30px;
            color: #555;
            font-size: 0.85rem;
            border-top: 1px solid rgba(255,255,255,0.05);
        }

        @media (max-width: 600px) {
            .form-grid { grid-template-columns: 1fr; }
            .dynamic-row { grid-template-columns: 1fr 1fr 60px 35px; }
            .hero h2 { font-size: 1.8rem; }
        }
    </style>
</head>
<body>

<header>
    <div class="logo">📅</div>
    <div>
        <h1>Timetable Generator</h1>
        <p>Automated Weekly Class Schedule System</p>
    </div>
</header>

<div class="container">

    <!-- Hero -->
    <div class="hero">
        <h2>Generate Your Weekly Timetable</h2>
        <p>Add your subjects, teachers, and preferences — the system automatically builds a conflict-free schedule in seconds.</p>
    </div>

    <!-- Feature Cards -->
    <div class="cards">
        <div class="card">
            <div class="icon">🚫</div>
            <h3>No Conflicts</h3>
            <p>Teachers are never double-booked in the same time slot</p>
        </div>
        <div class="card">
            <div class="icon">📊</div>
            <h3>Even Distribution</h3>
            <p>Subjects spread evenly across the week by frequency</p>
        </div>
        <div class="card">
            <div class="icon">🔄</div>
            <h3>No Repetition</h3>
            <p>Same subject won't appear in back-to-back slots</p>
        </div>
        <div class="card">
            <div class="icon">🏫</div>
            <h3>Room Assignment</h3>
            <p>Classrooms and labs assigned automatically</p>
        </div>
    </div>

    <!-- Form -->
    <div class="form-section">
        <h2>⚙️ Configure Your Timetable</h2>

        <form action="generate" method="post">

            <!-- Subjects -->
            <div class="form-group full">
                <label>📚 Subjects (Name | Code | Periods/Week)</label>
                <div class="dynamic-list" id="subjectList">
                    <div class="dynamic-row">
                        <input type="text" name="subjectName" placeholder="e.g. Mathematics" required>
                        <input type="text" name="subjectCode" placeholder="e.g. MATH" required>
                        <input type="number" name="subjectPeriods" placeholder="5" min="1" max="10" value="4" required>
                        <button type="button" class="remove-btn" onclick="removeRow(this)">✕</button>
                    </div>
                </div>
                <button type="button" class="add-btn" onclick="addSubjectRow()">+ Add Subject</button>
            </div>

            <div class="divider">Teachers</div>

            <!-- Teachers -->
            <div class="form-group full">
                <label>👨‍🏫 Teachers (Name | ID | Subject Code | Max Periods/Day)</label>
                <div class="dynamic-list" id="teacherList">
                    <div class="dynamic-row" style="grid-template-columns:1fr 80px 1fr 80px 40px">
                        <input type="text" name="teacherName" placeholder="e.g. Alice Johnson" required>
                        <input type="text" name="teacherId" placeholder="T001" required>
                        <input type="text" name="teacherSubjects" placeholder="MATH,PHY" required>
                        <input type="number" name="teacherMaxPeriods" placeholder="4" min="1" max="8" value="4" required>
                        <button type="button" class="remove-btn" onclick="removeRow(this)">✕</button>
                    </div>
                </div>
                <button type="button" class="add-btn" onclick="addTeacherRow()">+ Add Teacher</button>
            </div>

            <div class="divider">Schedule Settings</div>

            <div class="form-grid">
                <!-- Days -->
                <div class="form-group">
                    <label>📅 Working Days</label>
                    <select name="includeSaturday">
                        <option value="no">Monday – Friday (5 days)</option>
                        <option value="yes">Monday – Saturday (6 days)</option>
                    </select>
                </div>

                <!-- Periods -->
                <div class="form-group">
                    <label>⏰ Periods Per Day</label>
                    <select name="periodsPerDay">
                        <option value="5">5 periods</option>
                        <option value="6" selected>6 periods</option>
                        <option value="7">7 periods</option>
                        <option value="8">8 periods</option>
                    </select>
                </div>

                <!-- Rooms -->
                <div class="form-group full">
                    <label>🏫 Classrooms (comma-separated, e.g. R101,R102,LAB1)</label>
                    <input type="text" name="rooms" placeholder="R101, R102, R103, LAB1" value="R101,R102,R103,LAB1">
                </div>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">🚀 Generate Timetable</button>
                <button type="button" class="btn btn-demo" onclick="loadDemo()">⚡ Load Demo Data</button>
            </div>

        </form>
    </div>

</div>

<footer>
    <p>Timetable Generator v1.0 &nbsp;|&nbsp; Built with Java Servlets &amp; JSP</p>
</footer>

<script>
    function addSubjectRow() {
        const list = document.getElementById('subjectList');
        const row = document.createElement('div');
        row.className = 'dynamic-row';
        row.innerHTML = `
            <input type="text" name="subjectName" placeholder="Subject Name" required>
            <input type="text" name="subjectCode" placeholder="Code" required>
            <input type="number" name="subjectPeriods" placeholder="4" min="1" max="10" value="4" required>
            <button type="button" class="remove-btn" onclick="removeRow(this)">✕</button>`;
        list.appendChild(row);
    }

    function addTeacherRow() {
        const list = document.getElementById('teacherList');
        const row = document.createElement('div');
        row.className = 'dynamic-row';
        row.style.gridTemplateColumns = '1fr 80px 1fr 80px 40px';
        row.innerHTML = `
            <input type="text" name="teacherName" placeholder="Teacher Name" required>
            <input type="text" name="teacherId" placeholder="T00X" required>
            <input type="text" name="teacherSubjects" placeholder="MATH,PHY" required>
            <input type="number" name="teacherMaxPeriods" placeholder="4" min="1" max="8" value="4" required>
            <button type="button" class="remove-btn" onclick="removeRow(this)">✕</button>`;
        list.appendChild(row);
    }

    function removeRow(btn) {
        const list = btn.closest('.dynamic-list');
        if (list.children.length > 1) btn.closest('.dynamic-row').remove();
    }

    function loadDemo() {
        // Clear existing rows
        document.getElementById('subjectList').innerHTML = '';
        document.getElementById('teacherList').innerHTML = '';

        const subjects = [
            ['Mathematics','MATH','5'],
            ['Physics','PHY','4'],
            ['Chemistry','CHEM','3'],
            ['English','ENG','4'],
            ['Computer Science','CS','4'],
            ['History','HIST','3'],
            ['Physical Education','PE','2']
        ];

        const teachers = [
            ['Alice Johnson','T001','MATH','4'],
            ['Bob Smith','T002','PHY,CHEM','4'],
            ['Carol White','T003','ENG','4'],
            ['David Lee','T004','CS','4'],
            ['Eva Brown','T005','HIST,PE','4']
        ];

        subjects.forEach(([name, code, periods]) => {
            const list = document.getElementById('subjectList');
            const row = document.createElement('div');
            row.className = 'dynamic-row';
            row.innerHTML = `
                <input type="text" name="subjectName" value="${name}" required>
                <input type="text" name="subjectCode" value="${code}" required>
                <input type="number" name="subjectPeriods" value="${periods}" min="1" max="10" required>
                <button type="button" class="remove-btn" onclick="removeRow(this)">✕</button>`;
            list.appendChild(row);
        });

        teachers.forEach(([name, id, subs, max]) => {
            const list = document.getElementById('teacherList');
            const row = document.createElement('div');
            row.className = 'dynamic-row';
            row.style.gridTemplateColumns = '1fr 80px 1fr 80px 40px';
            row.innerHTML = `
                <input type="text" name="teacherName" value="${name}" required>
                <input type="text" name="teacherId" value="${id}" required>
                <input type="text" name="teacherSubjects" value="${subs}" required>
                <input type="number" name="teacherMaxPeriods" value="${max}" min="1" max="8" required>
                <button type="button" class="remove-btn" onclick="removeRow(this)">✕</button>`;
            list.appendChild(row);
        });

        document.querySelector('select[name="periodsPerDay"]').value = '7';
        document.querySelector('input[name="rooms"]').value = 'R101,R102,R103,LAB1';
    }
</script>

</body>
</html>
