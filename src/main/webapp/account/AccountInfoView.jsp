<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Account Info View</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container mt-4">

		<!-- 계좌 정보 -->
		<div class="card shadow-sm rounded-3 mb-4">
			<div class="card-body d-flex justify-content-between align-items-center">
				<h5 class="m-0 fw-bold">${account.user_id}님의 계좌 정보</h5>
				<small class="text-muted">총 잔액: ${account.account_amount}원</small>
			</div>
		</div>

		<!-- 송금 내역 -->
		<div class="card shadow-sm rounded-3 mb-4">
			<div class="card-body">
				<h5 class="fw-bold mb-3">송금 내역</h5>

				<!-- 송금 내역이 없을 경우 메시지 표시 -->
				<c:if test="${empty transferHistory}">
					<p class="text-muted">송금 내역이 없습니다.</p>
				</c:if>

				<!-- 송금 내역을 테이블 형식으로 표시 -->
				<c:if test="${not empty transferHistory}">
					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<th scope="col">송금 계좌</th>
								<th scope="col">받는 계좌</th>
								<th scope="col">송금 금액</th>
								<th scope="col">송금 시간</th>
								<th scope="col">태그</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="transfer" items="${transferHistory}">
								<tr>
									<td>${transfer.senderAccount}</td>
									<td>${transfer.receiverAccount}</td>
									<td>${transfer.sendAmount}</td>
									<td>${transfer.sendTime}</td>
									<td>${transfer.tag}</td>
								</tr>
							</c:forEach>

						</tbody>
					</table>
				</c:if>
			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
