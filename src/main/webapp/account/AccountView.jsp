<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
      <div class="card-body d-flex justify-content-center align-items-center bg-danger rounded-3" style="height: 150px;">
        <!-- 사용자 이름과 총 잔액 (가운데 정렬, 세로 크기 증가, 텍스트 크기 증가) -->
        <div class="text-white text-center w-100">
          <h5 class="m-0 fw-bold text-dark" style="font-size: 1.5rem;">홍길동<small class="text-muted align-baseline ms-1" style="font-size: 1rem;">님</small></h5>
          <small class="text-muted" style="font-size: 1.25rem;">총 잔액: 1,234,567원</small>
        </div>
      </div>
      
      <!-- 톱니바퀴와 로그아웃 버튼을 오른쪽 상단에 배치 -->
      <div class="position-absolute top-0 end-0 p-2 d-flex gap-2">
        <button class="btn btn-outline-secondary btn-sm rounded-circle" title="설정">
          ⚙️
        </button>
      </div>
    </div>

    <!-- 계좌 정보 -->
    <div class="card shadow-sm rounded-3 mb-3">
      <div class="card-body d-flex justify-content-between align-items-center">
        <div>
          <p class="m-0">계좌번호: 123-456-789</p>
          <p class="m-0 text-muted">잔액: 500,000원</p>
        </div>
        <button class="btn btn-primary px-4 py-2 rounded-3">송금</button>
      </div>
    </div>

    <div class="card shadow-sm rounded-3 mb-3">
      <div class="card-body d-flex justify-content-between align-items-center">
        <div>
          <p class="m-0">계좌번호: 987-654-321</p>
          <p class="m-0 text-muted">잔액: 734,567원</p>
        </div>
        <button class="btn btn-primary px-4 py-2 rounded-3">송금</button>
      </div>
    </div>

    <!-- 추가 + 버튼들 -->
    <div class="d-flex flex-column justify-content-center align-items-center gap-3">
      <button class="btn btn-purple w-75 fs-3 py-3 rounded-pill">+</button>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>