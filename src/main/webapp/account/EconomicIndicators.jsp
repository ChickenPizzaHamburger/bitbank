<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div id="carouselExample1" class="carousel slide"
	data-bs-ride="carousel" data-bs-interval="5000">
	<!-- 인디케이터 추가 -->
	<div class="carousel-indicators">
		<button type="button" data-bs-target="#carouselExample1"
			data-bs-slide-to="0" class="active" aria-current="true"
			aria-label="Slide 1"></button>
		<button type="button" data-bs-target="#carouselExample1"
			data-bs-slide-to="1" aria-label="Slide 2"></button>
		<button type="button" data-bs-target="#carouselExample1"
			data-bs-slide-to="2" aria-label="Slide 3"></button>
	</div>
	<div class="carousel-inner">
		<!-- ESI 그래프 -->
		<div class="carousel-item active">
			경제심리지수 (ESI)
			<canvas id="esiChart"></canvas>
		</div>
		<!-- CPI 그래프 -->
		<div class="carousel-item">
			소비자물가지수 (CPI)
			<canvas id="cpiChart"></canvas>
		</div>
		<!-- PPI 그래프 -->
		<div class="carousel-item">
			생산자물가지수 (PPI)
			<canvas id="ppiChart"></canvas>
		</div>
	</div>
</div>

<script>
        // 서블릿에서 전달된 데이터
        var datesESI = ${empty esiDates ? '[]' : esiDates};
        var ratesESI = ${empty esiRates ? '[]' : esiRates};
        
        var datesCPI = ${empty cpiDates ? '[]' : cpiDates};
        var ratesCPI = ${empty cpiRates ? '[]' : cpiRates};
        
        var datesPPI = ${empty ppiDates ? '[]' : ppiDates};
        var ratesPPI = ${empty ppiRates ? '[]' : ppiRates};
/* 
        // 데이터가 올바르게 전달되었는지 확인하기 위한 콘솔 출력
        console.log("ESI Rates:", ratesESI);
        console.log("CPI Rates:", ratesCPI);
        console.log("PPI Rates:", ratesPPI); */

        // ESI 그래프 그리기
        var ctxESI = document.getElementById('esiChart').getContext('2d');
        var esiChart = new Chart(ctxESI, {
            type: 'line',
            data: {
                labels: datesESI, // 날짜
                datasets: [{
                    label: '경제심리지수(ESI)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    data: ratesESI,
                    fill: false
                }]
            },
            options: {
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '날짜'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: '경제지표'
                        },
                        beginAtZero: false
                    }
                }
            }
        });

        // CPI 그래프 그리기
        var ctxCPI = document.getElementById('cpiChart').getContext('2d');
        var cpiChart = new Chart(ctxCPI, {
            type: 'line',
            data: {
                labels: datesCPI, // 날짜
                datasets: [{
                    label: '소비자물가지수(CPI)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    data: ratesCPI,
                    fill: false
                }]
            },
            options: {
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '날짜'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: '경제지표'
                        },
                        beginAtZero: false
                    }
                }
            }
        });

        // PPI 그래프 그리기
        var ctxPPI = document.getElementById('ppiChart').getContext('2d');
        var ppiChart = new Chart(ctxPPI, {
            type: 'line',
            data: {
                labels: datesPPI, // 날짜
                datasets: [{
                    label: '생산자물가지수(PPI)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    data: ratesPPI,
                    fill: false
                }]
            },
            options: {
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '날짜'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: '경제지표'
                        },
                        beginAtZero: false
                    }
                }
            }
        });
    </script>
