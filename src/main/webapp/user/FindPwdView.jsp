<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>비밀번호 찾기</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script>
    let countdownTimer;
    let timeLeft;

    function startCountdown(initialTimeLeft) {
    	  timeLeft = initialTimeLeft;
    	  clearInterval(countdownTimer);
    	  
    	  countdownTimer = setInterval(function() {
    	    if (timeLeft <= 0) {
    	      clearInterval(countdownTimer);
    	      $('#countdown').text('0:00').css('color', 'red');
    	      $('#hp2Msg').text('인증 시간이 만료되었습니다. 인증번호를 다시 받아주세요').css('color', 'red');
    	      $('#verificationSection').hide(); 
    	      $('#timeExpiredMessage').show();
    	      
    	      // 타이머가 끝나면 버튼 섹션과 타이머 섹션을 숨기고 시간 초과 메시지를 보여줍니다
    	      $('#verificationBtnSection').hide();
    	      $('#timerSection').hide();
    	      $('#timeExpiredMessage').show();
    	    } else {
    	      let minutes = Math.floor(timeLeft / 60);
    	      let seconds = timeLeft % 60;
    	      $('#countdown').text(minutes + ':' + (seconds < 10 ? '0' : '') + seconds);
    	    }
    	    timeLeft--;
    	  }, 1000);
    	}

    function enableVerificationCode(event) {
      event.preventDefault();

      $.ajax({
        url: 'findPwdAction.use',
        method: 'POST',
        data: $('#findPwdForm').serialize(),
        success: function(response) {
          if (response.success) {
            let initialTimeLeft = 3 * 60;
            startCountdown(initialTimeLeft);
            $('#verificationSection').show();
            $('#verificationCode').prop('disabled', false);
            $('#verificationCodeBtn').prop('disabled', false);
            $('#verificationBtnSection').show();
            $('#timeExpiredMessage').hide();
            $('#timerSection').show(); // 타이머 섹션을 보여줍니다
          } else {
            alert(response.error);
          }
        },
        error: function(xhr, status, error) {
          console.error("AJAX Error:", status, error);
          alert(`서버 오류가 발생했습니다. 상태 코드: ${status}, 상세 오류: ${error}`);
        }
      });
    }

    function submitVerificationCode() {
      const verificationCode = $('#verificationCode').val();
      if (!verificationCode) {
        alert('인증번호를 입력해주세요.');
        return;
      }

      $.ajax({
        url: 'verifyCodeAction.use',
        method: 'POST',
        data: { code: verificationCode },
        dataType: 'json',
        success: function(response) {
          if (response.success) {
            window.location.href = 'findPwdResultView.use';
          } else {
            alert(response.message);
          }
        },
        error: function() {
          alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
        }
      });
    }
  </script>
</head>
<body>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow-sm rounded-3" style="width: 400px;">
      <div class="card-body">
        <h5 class="card-title text-center mb-4">비밀번호 찾기</h5>

        <form action="findPwdAction.use" method="POST" id="findPwdForm">
          <div class="mb-3">
            <label for="id" class="form-label">아이디</label>
            <input type="text" class="form-control" id="id" name="id" placeholder="아이디를 입력해주세요" required>
          </div>

          <div class="mb-3">
            <label for="email" class="form-label">등록된 이메일 주소</label>
            <input type="email" class="form-control" id="email" name="email" placeholder="이메일을 입력해주세요" required>
          </div>

          <div class="mb-3">
            <button type="button" class="btn btn-primary w-100" onclick="enableVerificationCode(event);">비밀번호 재설정 코드 보내기</button>
          </div>
        </form>

        <div class="mb-3" style="display:none;" id="verificationSection">
          <label for="verificationCode" class="form-label">인증번호</label>
          <input type="text" class="form-control" id="verificationCode" placeholder="이메일로 받은 인증번호 입력" disabled required>
        </div>

        <div class="mb-3" style="display:none;" id="verificationBtnSection">
          <button type="button" class="btn btn-primary w-100" id="verificationCodeBtn" onclick="submitVerificationCode();" disabled>인증번호 확인</button>
        </div>

        <div class="text-center mb-3" id="timerSection" style="display:none;">
          <small>인증번호 입력 시간: <span id="countdown">3:00</span></small>
        </div>

        <div class="text-center text-danger" id="timeExpiredMessage" style="display:none;">
          <small>인증번호 입력 시간이 초과되었습니다. 다시 시도해주세요.</small>
        </div>

        <div class="text-center">
          <small>비밀번호를 기억하셨다면 <a href="loginView.use">로그인</a></small>
        </div>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
