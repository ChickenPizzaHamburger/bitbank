<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>회원가입</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background-color: #f7f7f7;
      font-family: 'Poppins', sans-serif;
    }
    .container {
      max-width: 700px;
      margin: 50px auto;
      padding: 20px;
      background-color: white;
      border-radius: 15px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
    h2 {
      font-size: 2rem;
      text-align: center;
      font-weight: bold;
      color: #333;
      margin-bottom: 30px;
    }
    .form-section {
      padding: 20px;
      margin-bottom: 20px;
      border: 1px solid #ddd;
      border-radius: 20px;
      background-color: #f9f9f9;
    }
    .form-section:first-of-type {
      border-top-left-radius: 20px;
      border-top-right-radius: 20px;
    }
    .form-section:last-of-type {
      border-bottom-left-radius: 20px;
      border-bottom-right-radius: 20px;
    }
    .form-control {
      border-radius: 10px;
      padding: 10px;
      font-size: 1rem;
    }
    .gender-selection {
      display: flex;
      justify-content: center;
      gap: 10px;
    }
    .gender-selection input[type="radio"] {
      display: none;
    }
    .gender-selection label {
      width: 100px;
      padding: 10px;
      border: 2px solid #007bff;
      border-radius: 50px;
      text-align: center;
      cursor: pointer;
    }
    .gender-selection input[type="radio"]:checked + label {
      background-color: #007bff;
      color: white;
    }
    .form-actions {
      text-align: center;
    }
    .submit-btn {
      background-color: #4A90E2;
      border: none;
      color: white;
      padding: 12px 20px;
      font-size: 1.1rem;
      border-radius: 50px;
      width: 100%;
      margin-bottom: 10px;
    }
    .submit-btn:hover {
      background-color: #357ab7;
    }
    .reset-btn {
      background-color: #f5f5f5;
      border: 1px solid #ddd;
      color: #333;
      padding: 12px 20px;
      font-size: 1.1rem;
      border-radius: 50px;
      width: 100%;
    }
    .reset-btn:hover {
      background-color: #e1e1e1;
    }
  </style>
</head>
<body>
  <div class="container">
    <form method="post" action="./signupAction.use" onsubmit="return validateSignup();">
      <h2>회원가입</h2>

      <!-- 첫 번째 폼 그룹: 아이디, 비밀번호, 이메일 -->
      <div class="form-section">
        <input type="text" id="userId" name="userId" placeholder="아이디" class="form-control mb-3" autocomplete="off" required>
        <input type="password" id="userPwd" name="userPwd" placeholder="비밀번호" class="form-control mb-3" required>
        <input type="email" id="userEmail" name="userEmail" placeholder="이메일" class="form-control" required>
      </div>

      <!-- 두 번째 폼 그룹: 이름, 생년월일, 성별 -->
      <div class="form-section">
        <input type="text" id="userName" name="userName" placeholder="이름" class="form-control mb-3" required>
        <input type="date" id="birthDate" name="birthDate" class="form-control mb-3" required>

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
  </div>
</body>
</html>
