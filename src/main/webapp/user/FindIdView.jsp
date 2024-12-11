<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>아이디 찾기</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow-sm rounded-3" style="width: 400px;">
      <div class="card-body">
        <h5 class="card-title text-center mb-4">아이디 찾기</h5>
        
        <!-- 아이디 찾기 폼 -->
        <form action="findIdAction.use" method="POST">
          <div class="mb-3">
            <label for="email" class="form-label">이메일 주소</label>
            <input type="email" class="form-control" id="email" name="userEmail" placeholder="이메일을 입력하세요" required>
          </div>

          <div class="mb-3">
            <label for="birthdate" class="form-label">생년월일</label>
            <input type="text" class="form-control" id="birthdate" name="birthDate" placeholder="생년월일을 입력하세요 (예: 1999-08-17)" required>
          </div>
          
          <div class="mb-3">
            <button type="submit" class="btn btn-primary w-100">아이디 찾기</button>
          </div>
        </form>

        <!-- 돌아가기 버튼 -->
        <div class="text-center">
          <a href="loginView.use" class="btn btn-link">돌아가기</a>
        </div>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
