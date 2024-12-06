<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Account Info View</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
  <div class="container mt-4">

    <!-- 통장 정보 -->
    <div class="card shadow-sm rounded-3 mb-4">
      <div class="card-body d-flex justify-content-between align-items-center">
        <div>
          <h5 class="m-0 fw-bold">
            홍길동<small class="text-muted align-baseline ms-1" style="font-size: 0.75rem;">님</small>
          </h5>
          <small class="text-muted">총 잔액: 1,234,567원</small>
        </div>
        <button class="btn btn-light border rounded-circle btn-sm">⚙️ 설정</button>
      </div>
    </div>

    <!-- 이체 내역 -->
    <div class="card shadow-sm rounded-3 mb-4">
      <div class="card-body">
        <h5 class="fw-bold mb-3">이체 내역</h5>
        <div class="list-group">
          <div class="list-group-item d-flex justify-content-between align-items-center">
            <div>
              <p class="m-0">받는 사람: 김철수</p>
              <p class="m-0 text-muted">이체 금액: 100,000원</p>
            </div>
            <span class="badge bg-primary">완료</span> <!-- 파란색 배지 -->
          </div>
          <div class="list-group-item d-flex justify-content-between align-items-center">
            <div>
              <p class="m-0">받는 사람: 이영희</p>
              <p class="m-0 text-muted">이체 금액: 50,000원</p>
            </div>
            <span class="badge bg-primary">완료</span> <!-- 파란색 배지 -->
          </div>

          <div class="list-group-item d-flex justify-content-between align-items-center">
            <div>
              <p class="m-0">받는 사람: 최지훈</p>
              <p class="m-0 text-muted">이체 금액: 300,000원</p>
            </div>
            <span class="badge bg-danger">실패</span> <!-- 빨간색 배지 -->
          </div>
        </div>
      </div>
    </div>

  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>