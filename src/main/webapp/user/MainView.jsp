<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Bootstrap Layout</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	rel="stylesheet">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Gowun+Dodum&display=swap"
	rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
.gowun-dodum-regular {
	font-family: "Gowun Dodum", sans-serif;
	font-weight: 570;
	font-style: normal;
}

.carousel-item canvas {
	max-width: 100%;
	height: auto;
}

.carousel-indicators {
	position: absolute;
	top: 10px;
	left: 35%;
	transform: translateX(-50%);
	z-index: 1000;
}

.carousel-indicators button {
	background-color: rgba(129, 129, 129, 0);
	border: none;
	width: 10px;
	height: 10px;
	border-radius: 50%;
	margin: 0 5px;
}

.carousel-indicators .active {
	background-color: rgba(17, 17, 17);
}
</style>
</head>

<body style="display: block; height: 100vh;">

	<%
	// 로그인 상태 확인
	String userId = (String) session.getAttribute("userId");
	String baseURL = "http://localhost:8080/BitBank";
	String targetPage = userId != null ? baseURL + "/accountView.ac" : baseURL + "/loginView.use";
	%>

	<script>
		var isLoggedIn = <%=userId != null ? "true" : "false"%>;
        // DOM이 로드된 후 이벤트를 등록합니다.
        document.addEventListener("DOMContentLoaded", () => {
            const loginBtn = document.getElementById("login-btn");
            const signupBtn = document.getElementById("signup-btn");
            const logoutBtn = document.getElementById("logout-btn");
            const introduceBtn = document.getElementById("introduce-btn");            
            const accountaddBtn = document.getElementById("accountadd-btn");
            const sendlistBtn = document.getElementById("sendlist-btn");
            const iframe = document.querySelector("iframe[name='contentFrame']");

            //login 버튼을 눌렀을 때 동작
            loginBtn.addEventListener("click", () => {
                const newTargetPage = "<%=baseURL%>/loginView.use"; // 로그인 페이지 URL
                iframe.src = newTargetPage; // iframe src를 업데이트
            });
            
          	//signup 버튼을 눌렀을 때 동작
            signupBtn.addEventListener("click", () => {
                const newTargetPage = "<%=baseURL%>/signupView.use"; // 회원가입 페이지 URL
                iframe.src = newTargetPage; // iframe src를 업데이트
            });
			
            //logout 버튼을 눌렀을 때 동작
            logoutBtn.addEventListener("click", () => {
                const newTargetPage = "<%=baseURL%>/LogoutAction.use"; // 로그아웃 페이지 URL
                iframe.src = newTargetPage; // iframe src를 업데이트
                window.location.reload(); // 페이지 전체를 리로드
            });

            //회사 소개 눌렀을 때
            introduceBtn.addEventListener("click", () => {
            	location.href = "<%=baseURL%>/introduceView.use";
            });
            
            //계좌 개설을 눌렀을 때
            accountaddBtn.addEventListener("click", () => {
            	 if (!isLoggedIn) {
                     alert("로그인 후 이용해 주세요");
                 } else {
                	 const newTargetPage = "<%=baseURL%>/accountAddView.ac"; // 계좌개설 페이지 URL
                     iframe.src = newTargetPage; // iframe src를 업데이트
                 }
            });
            
            //전체 송금내역 눌렀을 때
            sendlistBtn.addEventListener("click", () => {
            	if (!isLoggedIn) {
                    alert("로그인 후 이용해 주세요");
                } else {
                	 const newTargetPage = "<%=baseURL%>/accountAddView.ac"; // 전체송금내역 페이지 URL
                	 iframe.src = newTargetPage; // iframe src를 업데이트
                }
            });
            
        });
    </script>

	<div class="container-fluid p-0 gowun-dodum-regular">
		<header class="bg-primary border-bottom">
			<nav
				class="navbar navbar-expand-md navbar-light container text-bg-primary">
				<a href="index.use"
					class="navbar-brand text-white d-flex align-items-center"> <i
					class="bi bi-bank fs-3"></i> <span class="ms-2 fw-bold">BitBank</span>
				</a>
				<button class="navbar-toggler" type="button"
					data-bs-toggle="collapse" data-bs-target="#navbarNav"
					aria-controls="navbarNav" aria-expanded="false"
					aria-label="Toggle navigation">
					<span class="navbar-toggler-icon"></span>
				</button>
				<div class="collapse navbar-collapse" id="navbarNav">
					<ul class="navbar-nav mx-auto gap-3">
						<li class="nav-item"><button class="btn btn-primary me-2"
								id="introduce-btn">오시는길</button></li>
						<li class="nav-item"><button class="btn btn-primary me-2"
								id="accountadd-btn">계좌개설</button></li>
						<li class="nav-item"><button class="btn btn-primary me-2"
								id="sendlist-btn">전체송금내역</button></li>
						<li class="nav-item"><button class="btn btn-primary me-2"
								id="accountadd-btn">일반통장</button></li>
						<li class="nav-item"><button class="btn btn-primary me-2"
								id="accountadd-btn">Lock대출</button></li>
					</ul>
					<div>
						<button class="btn btn-light me-2" id="login-btn"
							style="<%=userId == null ? "" : "display: none;"%>">Login</button>
						<button class="btn btn-light" id="signup-btn"
							style="<%=userId == null ? "" : "display: none;"%>">Sign-up</button>
						<button class="btn btn-light" id="logout-btn"
							style="<%=userId != null ? "" : "display: none;"%>">Logout</button>
					</div>
				</div>
			</nav>
		</header>

		<main class="container my-4">
			<div class="row">
				<div class="col-md-8">
					<div class="mb-4">
						<div class="card">
							<jsp:include page="/account/exchangeRate.jsp" />
						</div>
					</div>
					<div>
						<div class="card">
							<jsp:include page="/account/EconomicIndicators.jsp" />
						</div>
					</div>
				</div>

				<div class="col-md-4">
					<div class="card align-items-center mb-4">
						<div class="container-fluid" style="height: 48vh;">
							<main class="container-fluid p-0" style="height: 100%; width: 100%;">
								<iframe src="<%=targetPage%>" class="w-100 h-100 border-0"
									name="contentFrame"> </iframe>
							</main>
						</div>

					</div>
					<div class="container-fluid p-0">
						<jsp:include page="/NaverNews.jsp" />
					</div>
				</div>
			</div>
		</main>
		<hr>
		<footer class="bg-light text-center text-muted py-3">
			<p class="mb-0">© 1024–2024 BitBank. All rights reserved.</p>
		</footer>
	</div>

</body>

</html>
