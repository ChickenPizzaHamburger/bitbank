<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>회사 위치</title>

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Naver Maps API -->
<script type="text/javascript"
	src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=uhg1gie2kg"></script>
<!-- 부트스트랩 아이콘 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	rel="stylesheet">

<style>
body {
	font-family: "Arial", sans-serif;
	background-color: #f9f9f9;
}

.location-section {
	background-color: #fff;
	border: 1px solid #ddd;
	border-radius: 8px;
	padding: 20px;
	margin-bottom: 30px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.location-title {
	border-bottom: 2px solid #007bff;
	padding-bottom: 10px;
	margin-bottom: 20px;
}

.location-details {
	display: table;
	width: 100%;
	margin-bottom: 20px;
}

.location-details div {
	display: table-row;
}

.location-details div span {
	display: table-cell;
	padding: 5px 10px;
	vertical-align: middle;
}

.location-details div span:first-child {
	font-weight: bold;
	width: 150px;
}

.map-container {
	width: 100%;
	height: 400px;
	border-radius: 8px;
	margin-bottom: 20px;
}

.footer-info {
	font-size: 14px;
	color: #666;
}
</style>
</head>
<body>
	<%
	// 로그인 상태 확인
	String userId = (String) session.getAttribute("userId");
	String baseURL = "http://localhost:8080/BitBank(1211ver2)";
	String targetPage = userId != null ? baseURL + "/accountView.ac" : baseURL + "/loginView.use";
	%>
	<script>
        // DOM이 로드된 후 이벤트를 등록합니다.
        document.addEventListener("DOMContentLoaded", () => {
            const loginBtn = document.getElementById("login-btn");
            const signupBtn = document.getElementById("signup-btn");
            const logoutBtn = document.getElementById("logout-btn");
            
            loginBtn.addEventListener("click", () => {
            	location.href = "<%=baseURL%>/index.use";
            });
            
          		
            //logout 버튼을 눌렀을 때 동작
            logoutBtn.addEventListener("click", () => {
            	invalidateSession();
            	location.href = "<%=baseURL%>/index.use";
            });
        });
        function invalidateSession() {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "LogoutAction.use", true); // invalidateSession은 세션을 초기화하는 서블릿 경로
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    // 세션이 무효화된 후 처리할 코드
                    alert("로그아웃되었습니다.");
                }
            };
            xhr.send();
        }
        
        
    </script>
	<header class="bg-primary py-3">
		<div
			class="container d-flex justify-content-between align-items-center">
			<a href="index.use"
				class="text-white text-decoration-none d-flex align-items-center">
				<i class="bi bi-bank fs-3"></i> <span class="ms-2 fw-bold">BitBank</span>
			</a>
			<nav>
				<div class="collapse navbar-collapse" id="navbarNav">
					<ul class="navbar-nav mx-auto gap-3">
						<li class="nav-item"><a href="#" class="nav-link text-white">회사소개</a></li>
						<li class="nav-item"><a href="#" class="nav-link text-white">계좌개설</a></li>
						<li class="nav-item"><a href="#" class="nav-link text-white">전체송금내역</a></li>
					</ul>
				</div>
				<div>
					<button class="btn btn-light me-2" id="login-btn"
						style="<%=userId == null ? "" : "display: none;"%>">Login</button>
					<button class="btn btn-light" id="logout-btn"
						style="<%=userId != null ? "" : "display: none;"%>">Logout</button>
				</div>
			</nav>
		</div>
	</header>

	<main class="py-5">
		<div class="container-fluid">
			<div class="row justify-content-center">
				<div class="location-section col-md-5 me-5">
					<h2 class="location-title">강남센터</h2>
					<div id="map1" class="map-container"></div>
					<p class="text-muted">BitBank 강남센터 위치를 위 지도에서 확인하세요.</p>
					<div class="location-details">
						<div>
							<span>주소</span><span>서울 서초구 서초대로74길 33 3층 비트교육센터</span>
						</div>
						<div>
							<span>우편번호</span><span>06621</span>
						</div>
						<div>
							<span>지번</span><span>서초동 1327-33</span>
						</div>
						<div>
							<span>고객센터</span><span>02-1234-1234</span>
						</div>
					</div>
				</div>

				<div class="location-section col-md-5">
					<h2 class="location-title">비트플렉스센터</h2>
					<div id="map2" class="map-container"></div>
					<p class="text-muted">BitBank 비트플렉스센터 위치를 위 지도에서 확인하세요.</p>
					<div class="location-details">
						<div>
							<span>주소</span><span>서울 성동구 왕십리광장로 17</span>
						</div>
						<div>
							<span>우편번호</span><span>04750</span>
						</div>
						<div>
							<span>지번</span><span>행당동 168-151</span>
						</div>
						<div>
							<span>고객센터</span><span>02-5678-5678</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>

	<footer class="bg-light py-4">
		<div class="container text-left footer-info">
			<p>&copy; 2024 BitBank. All Rights Reserved.</p>
			<p>
				<strong>대표 고객센터 : </strong>
			</p>
			02-1234-5678
			<p>
				<strong>대표 이메일 : </strong> bit.bank.pwd@gmail.com
			</p>
			<p>
				<strong>대표 : </strong>김진우
			</p>
		</div>
	</footer>

	<!-- Naver Maps Script -->
	<script type="text/javascript">
		var map1 = new naver.maps.Map('map1', {
			center : new naver.maps.LatLng(37.494603144452405,
					127.02757774645144),
			zoom : 17
		});

		var marker1 = new naver.maps.Marker({
			position : new naver.maps.LatLng(37.494603144452405,
					127.02757774645144),
			map : map1
		});

		var map2 = new naver.maps.Map('map2', {
			center : new naver.maps.LatLng(37.56151074982931,
					127.03845376380954),
			zoom : 17
		});

		var marker2 = new naver.maps.Marker({
			position : new naver.maps.LatLng(37.56151074982931,
					127.03845376380954),
			map : map2
		});
	</script>

	<!-- Bootstrap Bundle JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
