<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error - Timetable Generator</title>
    <style>
        * { margin:0; padding:0; box-sizing:border-box; }
        body {
            font-family: 'Segoe UI', sans-serif;
            background: linear-gradient(135deg,#1a1a2e,#16213e,#0f3460);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #eee;
        }
        .box {
            background: rgba(255,255,255,0.05);
            border: 1px solid rgba(233,69,96,0.4);
            border-radius: 20px;
            padding: 50px 40px;
            text-align: center;
            max-width: 500px;
        }
        .icon { font-size: 4rem; margin-bottom: 20px; }
        h1 { font-size: 1.8rem; color: #e94560; margin-bottom: 15px; }
        p  { color: #aaa; line-height: 1.7; margin-bottom: 30px; }
        a  {
            display: inline-block;
            padding: 12px 30px;
            background: linear-gradient(135deg,#e94560,#c62a47);
            color: #fff;
            border-radius: 50px;
            text-decoration: none;
            font-weight: 600;
        }
        a:hover { opacity: 0.85; }
    </style>
</head>
<body>
    <div class="box">
        <div class="icon">⚠️</div>
        <h1>Something went wrong</h1>
        <p>
            <%= request.getAttribute("error") != null
                ? request.getAttribute("error")
                : "An unexpected error occurred while generating the timetable." %>
        </p>
        <a href="index.jsp">← Go Back</a>
    </div>
</body>
</html>
