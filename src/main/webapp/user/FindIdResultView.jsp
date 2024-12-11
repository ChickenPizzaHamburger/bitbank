<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>아이디 찾기 결과</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow-sm rounded-3" style="width: 400px;">
      <div class="card-body">
        <h5 class="card-title text-center mb-4">아이디 찾기 결과</h5>

        <!-- 아이디 찾기 결과 -->
        <div class="text-center mb-4">
          <p><strong>아이디: </strong>${userId}</p> <!-- 이 부분은 실제 아이디로 동적으로 채워져야 합니다. -->
        </div>

        <!-- 로그인 페이지로 돌아가기 버튼 -->
        <div class="text-center">
          <a href="loginView.use" class="btn btn-link">로그인 화면으로 돌아가기</a>
        </div>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
