<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- 자동 캐러셀 시작 -->
<div id="carouselExample" class="carousel slide" data-bs-ride="carousel"
	data-bs-interval="5000">
	<!-- 인디케이터 추가 -->
	<div class="carousel-indicators">
		<button type="button" data-bs-target="#carouselExample"
			data-bs-slide-to="0" class="active" aria-current="true"
			aria-label="Slide 1"></button>
		<button type="button" data-bs-target="#carouselExample"
			data-bs-slide-to="1" aria-label="Slide 2"></button>
		<button type="button" data-bs-target="#carouselExample"
			data-bs-slide-to="2" aria-label="Slide 3"></button>
	</div>
	<div class="carousel-inner">
		<!-- USD 그래프 -->
		<div class="carousel-item active">
			미국 달러 (USD)
			<canvas id="usdChart"></canvas>
		</div>
		<!-- JPY 그래프 -->
		<div class="carousel-item">
			일본 엔화 (JPY)
			<canvas id="jpyChart"></canvas>
		</div>
		<!-- EUR 그래프 -->
		<div class="carousel-item">
			유로 (EUR)
			<canvas id="eurChart"></canvas>
		</div>
	</div>
</div>
<!-- 자동 캐러셀 끝 -->

<script>
        // 서블릿에서 전달된 데이터
        var usdDates = ${usdDates};
        var usdRates = ${usdRates};
        
        var jpyDates = ${jpyDates};
        var jpyRates = ${jpyRates};
        
        var eurDates = ${eurDates};
        var eurRates = ${eurRates};
/* 
        // 콘솔로 데이터를 확인
        console.log("USD Dates:", usdDates);
        console.log("USD Rates:", usdRates);

        console.log("JPY Dates:", jpyDates);
        console.log("JPY Rates:", jpyRates);

        console.log("EUR Dates:", eurDates);
        console.log("EUR Rates:", eurRates);
 */
        // USD 그래프 그리기
        var ctxUSD = document.getElementById('usdChart').getContext('2d');
        var usdChart = new Chart(ctxUSD, {
            type: 'line',
            data: {
                labels: usdDates, 
                datasets: [{
                    label: '미국 달러 (USD)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    data: usdRates,
                    fill: false
                }]
            },
            options: {
                scales: {
                    x: {
                        title: { display: true, text: '날짜' }
                    },
                    y: {
                        title: { display: true, text: '환율' },
                        beginAtZero: false
                    }
                }
            }
        });

        // JPY 그래프 그리기
        var ctxJPY = document.getElementById('jpyChart').getContext('2d');
        var jpyChart = new Chart(ctxJPY, {
            type: 'line',
            data: {
                labels: jpyDates, 
                datasets: [{
                    label: '일본 엔화 (JPY)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    data: jpyRates,
                    fill: false
                }]
            },
            options: {
                scales: {
                    x: {
                        title: { display: true, text: '날짜' }
                    },
                    y: {
                        title: { display: true, text: '환율' },
                        beginAtZero: false
                    }
                }
            }
        });

        // EUR 그래프 그리기
        var ctxEUR = document.getElementById('eurChart').getContext('2d');
        var eurChart = new Chart(ctxEUR, {
            type: 'line',
            data: {
                labels: eurDates, 
                datasets: [{
                    label: '유로 (EUR)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    data: eurRates,
                    fill: false
                }]
            },
            options: {
                scales: {
                    x: {
                        title: { display: true, text: '날짜' }
                    },
                    y: {
                        title: { display: true, text: '환율' },
                        beginAtZero: false
                    }
                }
            }
        });
    </script>
