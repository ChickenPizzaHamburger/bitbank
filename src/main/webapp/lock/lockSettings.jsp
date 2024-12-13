<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>락 설정</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>락 설정</h1>
        <div class="mb-4">
            <h2>락 생성</h2>
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
        <div>
            <h2>락 목록</h2>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>락 ID</th>
                        <th>계좌 번호</th>
                        <th>락 설정 금액</th>
                        <th>락 시작 날짜</th>
                        <th>락 종료 날짜</th>
                        <th>상태</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="lock" items="${locks}">
                        <tr>
                            <td>${lock.lockId}</td>
                            <td>${lock.accountNum}</td>
                            <td>${lock.lockedAmount}</td>
                            <td>${lock.lockStartDate}</td>
                            <td>${lock.lockEndDate}</td>
                            <td><c:choose>
                                <c:when test="${lock.active}">활성</c:when>
                                <c:otherwise>비활성</c:otherwise>
                            </c:choose></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- 계좌 내역으로 돌아가기 버튼 -->
<div class="d-flex justify-content-center mt-4">
    <button class="btn btn-secondary px-4 py-2 rounded-3" onclick="location.href = './accountView.ac?account_num=${account_num}'">계좌 내역으로 돌아가기</button>
</div>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
