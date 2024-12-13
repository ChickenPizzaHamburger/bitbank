<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- 부트스트랩 모바일 설정 -->
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
    <title>로그인</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"> <!-- 부트스트랩 CSS -->
    <link rel="stylesheet" href="./resources/loginStyle.css"> <!-- CSS 파일 링크 -->
</head>
<body>
    <main>
        <form method="post" action="./loginAction.use" onsubmit="return valid();">
            <header class="col">
                <h2>로그인 페이지</h2>
            </header>

            <!-- 로그인 방식 선택 -->
           <section class="login-type">
    	   		<div class="login-option">
        			<input type="radio" name="login_type" value="default" id="login-default" checked onclick="toggleLoginForm('default')">
        			<label for="login-default" class="radio-label">기본</label>
    			</div>
    			<div class="login-option">
        			<input type="radio" name="login_type" value="social" id="login-social" onclick="toggleLoginForm('social')">
        			<label for="login-social" class="radio-label">소셜</label>
    			</div>
    			<!-- <div class="login-option">
        			<input type="radio" name="login_type" value="qr" id="login-qr" onclick="toggleLoginForm('qr')">
       				<label for="login-qr" class="radio-label">QR</label>
    			</div> -->
			</section>

            <!-- 일반 로그인 폼 -->
            <section id="default-login" class="login-section">
                <table>
                    <tr>
                        <td>아이디</td>
                        <td><input type="text" id="userId" name="userId" placeholder="아이디를 입력하세요" autocomplete="off" required></td>
                    </tr>
                    <tr>
                        <td>비밀번호</td>
                        <td><input type="password" id="userPwd" name="userPwd" placeholder="비밀번호를 입력하세요" required></td>
                    </tr>
                </table>
                <div class="form-actions">
                    <input type="submit" value="로그인" class="submit-btn">
                    <button type="reset" class="reset-btn">다시 입력</button>
                </div>
            </section>

            <!-- 소셜 로그인 -->
			<section id="social-login" class="login-section" style="display: none;">
				<p style="font-size: 0.9rem; line-height: 1.4;">
					소셜을 통해서 로그인하려면 버튼을 클릭하세요. <br> (자동 소셜 회원가입)
				</p>
				<a href="javascript:void(0);" onclick="requestKakaoLogin();">
                    <img src="./resources/kakaoLogin.png" alt="카카오 로그인 버튼" style="width: 200px; cursor: pointer;">
				</a>
			</section>

			<!-- QR 로그인 -->
            <!-- <section id="qr-login" class="login-section" style="display: none;">
                <p>QR 코드를 스캔하여 로그인하세요.</p>
                <img src="qr-placeholder.png" alt="QR 코드" style="max-width: 100px; margin-top: 10px;">
            </section> -->

            <!-- 항상 표시되는 링크 -->
            <footer id="login-links" class="form-footer">
                <div class="links">
                    <a href="./findIdView.use">아이디 찾기</a>
                    <a href="./findPwdView.use">비밀번호 찾기</a>
                </div>
            </footer>
        </form>
    </main>

    <!-- Toast 알림 -->
	<div class="position-fixed top-0 start-50 translate-middle-x p-3" style="z-index: 11;">
	    <div id="errorToast" class="toast align-items-center text-bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="true" data-bs-delay="3000">
	        <div class="d-flex">
	            <div class="toast-body">
	                아이디나 비밀번호가 틀렸습니다.
	            </div>
	            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
	        </div>
	    </div>
	</div>

    <!-- 서버에서 전달된 에러 메시지를 확인하여 Toast 표시 -->
    <script>
	    document.addEventListener("DOMContentLoaded", () => {
	        // 서버에서 전달된 errorMessage
	        const serverErrorMessage = '<%= request.getSession().getAttribute("errorMessage") != null ? (String) request.getSession().getAttribute("errorMessage") : "" %>';

	        // localStorage에 저장된 errorMessage
	        const localErrorMessage = localStorage.getItem("loginError");

	        // 두 가지 errorMessage 중 하나라도 있으면 토스트 메시지 표시
	        const errorMessage = serverErrorMessage || localErrorMessage;
	        if (errorMessage) {
	            // 토스트 메시지 표시 함수
	            const showErrorMessage = (message) => {
	                const toastElement = document.getElementById('errorToast');
	                if (toastElement) {
	                    const toastBody = toastElement.querySelector('.toast-body');
	                    if (toastBody) {
	                        toastBody.textContent = message;
	                        const toast = new bootstrap.Toast(toastElement);
	                        toast.show();
	                    }
	                }
	            };

	            showErrorMessage(errorMessage);

	            // localStorage에 저장된 errorMessage 삭제
	            localStorage.removeItem("loginError");

	            // 서버에서 errorMessage 삭제
	            <%
	                request.getSession().removeAttribute("errorMessage");
	            %>
	        }
	    });
	</script>

    <!-- 외부 자바스크립트 및 부트스트랩 JS -->
    <script src="./resources/loginMode.js"></script>
    <script src="./resources/kakaoLogin.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>