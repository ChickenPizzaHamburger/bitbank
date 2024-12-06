<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>비밀번호 재설정</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow-sm rounded-3" style="width: 400px;">
      <div class="card-body">
        <h5 class="card-title text-center mb-4">비밀번호 재설정</h5>

        <!-- 비밀번호 재설정 폼 -->
        <form>
          <div class="mb-3">
            <label for="newPassword" class="form-label">새 비밀번호</label>
            <input type="password" class="form-control" id="newPassword" placeholder="새 비밀번호 입력" required>
          </div>

          <div class="mb-3">
            <label for="confirmPassword" class="form-label">새 비밀번호 확인</label>
            <input type="password" class="form-control" id="confirmPassword" placeholder="새 비밀번호 확인" required>
          </div>

          <div class="mb-3">
            <button type="submit" class="btn btn-primary w-100">비밀번호 재설정</button>
          </div>
        </form>

        <!-- 비밀번호 재설정 완료 설명 -->
        <div class="text-center">
          <small>비밀번호를 찾으셨다면 <a href="loginView.use">로그인</a></small>
        </div>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
