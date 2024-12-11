<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>회원가입</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="./resources/signupStyle.css">
</head>
<body>
  <div class="container">
    <form id="signupForm" method="post" action="./signupAction.use" autocomplete="off">
      <h2>회원가입</h2>

      <!-- 첫 번째 폼 그룹: 아이디, 비밀번호, 이메일 -->
      <div class="form-section">
      	<small id="userIdError" class="text-danger d-block">${errorId}</small>
        <input type="text" id="userId" name="userId" placeholder="아이디" class="form-control mb-3" required>
        <small id="userPwdError" class="text-danger d-block">${errorPwd}</small>
        <input type="password" id="userPwd" name="userPwd" placeholder="비밀번호" class="form-control mb-3" required>
        <small id="userEmailError" class="text-danger d-block">${errorEmail}</small>
        <input type="email" id="userEmail" name="userEmail" placeholder="이메일" class="form-control" required>
      </div>

      <!-- 두 번째 폼 그룹: 이름, 생년월일, 성별 -->
      <div class="form-section">
      	<small id="userNameError" class="text-danger d-block">${errorName}</small>
        <input type="text" id="userName" name="userName" placeholder="이름" class="form-control mb-3" required>
        <input type="date" id="birthDate" name="birthDate" class="form-control mb-3" 
          min="1900-01-01" max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" 
          value="2000-01-01" required>

        <!-- 성별 선택 -->
        <div class="gender-selection mb-3">
          <input type="radio" id="male" name="gender" value="MALE">
          <label for="male">남성</label>
          <input type="radio" id="female" name="gender" value="FEMALE">
          <label for="female">여성</label>
          <input type="radio" id="none" name="gender" value="ANONY" checked>
          <label for="none">선택안함</label>
        </div>

        <!-- 확인 및 다시 하기 -->
        <div class="form-actions">
          <input type="submit" value="회원가입" class="submit-btn">
          <button type="reset" class="reset-btn">다시 입력</button>
        </div>
      </div>
    </form>

    <!-- 로그인 링크 추가 -->
    <div class="login-link">
      <small>이미 계정이 있으신가요? <a href="./loginView.use">로그인</a></small>
    </div>
  </div>

  <!-- 외부 자바스크립트 연결 -->
  <script src="./resources/signupVal.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script> <!-- 부트스트랩 JS -->
</body>
</html>