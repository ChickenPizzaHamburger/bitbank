<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Account View</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
	<div class="container mt-4 position-relative">
		<!-- 사용자 정보 -->
		<div class="card shadow-sm rounded-3 mb-4 position-relative">
			<div class="card-body d-flex justify-content-center align-items-center" style="background-color: #66b3ff; height: 150px;">
				<div class="text-white text-center w-100">
					<h5 class="m-0 fw-bold text-dark" style="font-size: 1.5rem;">
						<c:out value="${username}" />
						<small class="text-muted align-baseline ms-1" style="font-size: 1rem;">님</small>
					</h5>
					<small class="text-dark" style="font-size: 1.25rem;"> 총 잔액: <c:out value="${totalAmount}" />원</small>
				</div>
			</div>
		</div>

		<%-- <!-- 관리자 링크 -->
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <div class="mb-3">
                <a href="userListAction.use" class="btn btn-primary">유저 리스트 보기</a>
            </div>
        </c:if> --%>

		<!-- 계좌 정보 -->
		<c:forEach var="account" items="${accountList}">
			<div class="card shadow-sm rounded-3 mb-3">
				<div class="card-body d-flex justify-content-between align-items-center">
					<div>
						<p class="m-0">
							계좌번호: <c:out value="${account.account_num}" /> (<c:out value="${account.account_type}" />)
						</p>
						<p class="m-0 text-muted">
							잔액: <c:out value="${account.account_amount}" />원
						</p>
					</div>
					<div class="d-flex gap-1">
						<!-- 송금 버튼 -->
						<button class="btn btn-primary px-4 py-2 rounded-3" onclick="location.href = './sendListAction.tr?num=${account.account_num}'">송금</button>
						<!-- 계좌정보 버튼 -->
						<button class="btn btn-secondary px-4 py-2 rounded-3" onclick="location.href = './accountInfoAction.ac?accountNum=${account.account_num}'">계좌 내역</button>
						<!-- 락 설정 버튼 -->
						<button class="btn btn-warning px-4 py-2 rounded-3" onclick="location.href = './lock?action=settings&account_num=${account.account_num}'">락 설정</button>
					</div>
				</div>
			</div>
		</c:forEach>

		<!-- 추가 + 버튼 -->
		<div class="d-flex flex-column justify-content-center align-items-center gap-3">
			<button class="btn btn-purple w-75 fs-3 py-3 rounded-pill" onclick="location.href = './accountAddView.ac'">+</button>
		</div>
	</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>