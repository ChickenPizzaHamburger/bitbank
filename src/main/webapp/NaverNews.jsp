<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>BitNews</title>
<!-- 부트스트랩 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
/* 캐러셀의 크기, 디자인 수정 및 테두리 추가 */
#newsCarousel {
	max-width: 100%; /* 너비를 최대화 */
	height: 451px; /* 높이를 더 줄여서 더 가늘고 긴 형태로 설정 */
	border: 1px solid #ccc; /* 테두리 두께를 1px로 줄임 */
	border-radius: 8px; /* 테두리 모서리 둥글게 처리 */
	overflow: hidden; /* 테두리 밖으로 나가는 부분 잘림 */
}

.carousel-item {
	height: 100%; /* 아이템을 전체 높이에 맞게 설정 */
}

.carousel-inner {
	height: 100%;
}

.carousel-item .text-center {
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: center;
	color: #333;
	padding: 10px; /* 내부 패딩을 줄여서 여백을 줄임 */
}

h3 {
	font-size: 1rem; /* 제목 폰트 크기 줄이기 */
}

p {
	font-size: 0.8rem; /* 설명 폰트 크기 줄이기 */
}

small {
	font-size: 0.7rem; /* 작은 텍스트 폰트 크기 줄이기 */
}
</style>
</head>
<body>
	<div>
		<!-- 부트스트랩 캐러셀 -->
		<div id="newsCarousel" class="carousel slide" data-bs-ride="carousel">
			<div class="carousel-inner">
				<c:if test="${not empty newsList}">
					<c:forEach var="news" items="${newsList}" varStatus="status">
						<div class="carousel-item ${status.first ? 'active' : ''}">
							<div class="text-center">
								<h3>
									<a href="${news.link}" target="_blank">${news.title}</a>
								</h3>
								<p>${news.description}</p>
								<small>${news.pubDate}</small>
							</div>
						</div>
					</c:forEach>
				</c:if>

				<c:if test="${empty newsList}">
					<p>No news available.</p>
					<!-- 뉴스가 없을 때 표시할 메시지 -->
				</c:if>

			</div>
			<!-- 이전/다음 버튼 -->
			<button class="carousel-control-prev" type="button"
				data-bs-target="#newsCarousel" data-bs-slide="prev">
				<span class="carousel-control-prev-icon" aria-hidden="true"></span>
				<span class="visually-hidden">이전</span>
			</button>
			<button class="carousel-control-next" type="button"
				data-bs-target="#newsCarousel" data-bs-slide="next">
				<span class="carousel-control-next-icon" aria-hidden="true"></span>
				<span class="visually-hidden">다음</span>
			</button>
		</div>
	</div>

	<!-- 부트스트랩 JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
