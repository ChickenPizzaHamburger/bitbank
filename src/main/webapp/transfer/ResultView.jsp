<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>송금 결과</title>
</head>
<body>
    <h1>송금 결과</h1>

    <!-- 성공 또는 실패 메시지 표시 -->
    <c:choose>
        <c:when test="${transferSuccess}">
            <p>송금이 성공적으로 완료되었습니다.</p>
        </c:when>
        <c:otherwise>
            <p style="color: red;">송금에 실패했습니다. ${errorMessage}</p>
        </c:otherwise>
    </c:choose>
    <a href="sendListAction.tr">계좌 목록으로 돌아가기</a>
</body>
</html>
