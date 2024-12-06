<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>비밀번호 찾기</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script>
	// 서버에서 전달받은 만료 시간 (timestamp)
	<%-- let expirationTime =<%=expirationTime%>; // 서버에서 전달받은 timestamp --%>

	function startTimer() {
		// 현재 시간을 기준으로 남은 시간 계산
		let currentTime = new Date().getTime();
		let timeRemaining = expirationTime - currentTime;

		if (timeRemaining > 0) {
			let timerInterval = setInterval(
					function() {
						let minutes = Math.floor(timeRemaining / 60000);
						let seconds = Math
								.floor((timeRemaining % 60000) / 1000);

						document.getElementById('timer').textContent = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;

						if (timeRemaining <= 0) {
							clearInterval(timerInterval);
							document.getElementById('verificationCode').disabled = true;
							document.getElementById('timeExpiredMessage').style.display = 'block';
						}

						timeRemaining -= 1000;
					}, 1000);
		}
	}

	window.onload = startTimer;
</script>
</head>
<body>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow-sm rounded-3" style="width: 400px;">
      <div class="card-body">
        <h5 class="card-title text-center mb-4">비밀번호 찾기</h5>

        <!-- 비밀번호 찾기 폼 -->
        <form>
          <div class="mb-3">
            <label for="id" class="form-label">아이디</label>
            <input type="text" class="form-control" id="id" placeholder="아이디 입력" required>
          </div>

          <div class="mb-3">
            <label for="email" class="form-label">등록된 이메일 주소</label>
            <input type="email" class="form-control" id="email" placeholder="이메일 주소 입력" required>
          </div>

          <div class="mb-3">
            <button type="submit" class="btn btn-primary w-100">비밀번호 재설정 링크 보내기</button>
          </div>

          <!-- 인증번호 입력란 추가 -->
          <div class="mb-3">
            <label for="verificationCode" class="form-label">인증번호</label>
            <input type="text" class="form-control" id="verificationCode" placeholder="이메일로 받은 인증번호 입력" required>
          </div>

          <div class="mb-3">
            <button type="submit" class="btn btn-primary w-100">인증번호 확인</button>
          </div>
        </form>

        <!-- 타이머 표시 -->
        <div class="text-center mb-3">
          <small>인증번호 입력 시간: <span id="timer">3:00</span></small>
        </div>

        <!-- 시간 초과 메시지 -->
        <div class="text-center text-danger" id="timeExpiredMessage" style="display:none;">
          <small>인증번호 입력 시간이 초과되었습니다. 다시 시도해주세요.</small>
        </div>

        <!-- 비밀번호 찾기 설명 -->
        <div class="text-center">
          <small>비밀번호를 기억하셨다면 <a href="loginView.use">로그인</a></small>
        </div>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
