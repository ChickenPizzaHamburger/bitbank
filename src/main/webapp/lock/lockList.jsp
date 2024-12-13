<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>락 목록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>락 목록</h1>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>