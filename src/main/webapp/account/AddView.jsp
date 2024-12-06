<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>통장 개설</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow-sm rounded-3" style="width: 400px;">
      <div class="card-body">
        <h5 class="card-title text-center mb-4">통장 개설</h5>

        <!-- 통장 개설 폼 -->
        <form action="addAction.ac" method="POST">
          <!-- 비밀번호 입력 -->
          <div class="mb-3">
            <label for="account_password" class="form-label">비밀번호</label>
            <input type="password" class="form-control" id="account_password" name="account_password" required>
          </div>

          <!-- 통장 타입 선택 -->
          <div class="mb-3">
            <label for="account_type" class="form-label">통장 타입</label>
            <select class="form-select" id="account_type" name="account_type" required>
              <option value="COMMON">기본</option>
              <option value="PLUSBOX">플러스박스</option>
              <option value="INSTALLMENT">적금통장</option>
            </select>
          </div>
          
          <!-- 영업점 선택 -->
          <div class="mb-3">
            <label for="account_office" class="form-label">영업점</label>
            <select class="form-select" id="account_office" name="account_office" required>
              <option value="Gangnam">강남</option>
              <option value="Seocho">서초</option>
            </select>
          </div>

          <!-- 제출 버튼 -->
          <div class="d-flex justify-content-center">
            <button type="submit" class="btn btn-primary w-100">통장 개설</button>
          </div>
        </form>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
