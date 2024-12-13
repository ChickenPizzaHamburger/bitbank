<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>락 생성</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>락 생성</h1>
        <form action="lock" method="post">
            <input type="hidden" name="account_num" value="${account_num}">
            <div class="mb-3">
                <label for="locked_amount" class="form-label">락 설정 금액:</label>
                <input type="number" id="locked_amount" name="locked_amount" class="form-control" required>
            </div>
            <div class="mb-3">
                <label for="lock_start_date" class="form-label">락 시작 날짜:</label>
                <input type="datetime-local" id="lock_start_date" name="lock_start_date" class="form-control" required>
            </div>
            <div class="mb-3">
                <label for="lock_end_date" class="form-label">락 종료 날짜:</label>
                <input type="datetime-local" id="lock_end_date" name="lock_end_date" class="form-control" required>
            </div>
            <button type="submit" name="action" value="create" class="btn btn-primary">락 생성</button>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
