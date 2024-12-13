package net.user.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.util.Action;
import net.util.ActionForward;

public class EconomicIndicatorsAction implements Action {

	private static final String API_KEY = "AIAJ3DPRYSGSRCMO6E29"; // 발급받은 한국은행 API 키
	private static final String API_URL = "https://ecos.bok.or.kr/api/StatisticSearch/";

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		LocalDate endDate = LocalDate.now(); // 오늘 날짜
		LocalDate startDate = endDate.minusMonths(12); // 1 년 전 날짜

		// 날짜 포맷 설정 (yyyyMM)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
		String startDateStr = startDate.format(formatter);
		String endDateStr = endDate.format(formatter);

		// 지표 코드 설정
		String[] characteristic = { "E1000", "0", "*AA" }; // 경제심리지수, 소비자물가지수, 생산자물가지수
		String[] characteristicName = { "ESI", "CPI", "PPI" }; // 지표 이름

		StringBuilder esiDates = new StringBuilder();
		StringBuilder esiRates = new StringBuilder();

		StringBuilder cpiDates = new StringBuilder();
		StringBuilder cpiRates = new StringBuilder();

		StringBuilder ppiDates = new StringBuilder();
		StringBuilder ppiRates = new StringBuilder();

		// 지표 코드에 대해 데이터 요청
		for (int i = 0; i < characteristic.length; i++) {
			String price = characteristic[i];
			String urlString = "";

			// 지표에 따라 다른 URL 형성
			if ("E1000".equals(price)) {
				urlString = String.format("%s%s/json/kr/1/100/513Y001/M/%s/%s/%s", API_URL, API_KEY, startDateStr,
						endDateStr, price);
			} else if ("0".equals(price)) {
				urlString = String.format("%s%s/json/kr/1/100/901Y009/M/%s/%s/%s", API_URL, API_KEY, startDateStr,
						endDateStr, price);
			} else if ("*AA".equals(price)) {
				urlString = String.format("%s%s/json/kr/1/100/404Y014/M/%s/%s/%s", API_URL, API_KEY, startDateStr,
						endDateStr, price);
			}

			// API 요청
			String jsonResponse = sendRequest(urlString);

			// HTTP 상태 코드 확인
			if (jsonResponse == null || jsonResponse.contains("error")) {
				continue; // 오류가 있을 경우 넘어가기
			}

			// JSON 응답 파싱 (응답 구조가 올바른지 확인)
			JsonObject jsonObject = com.google.gson.JsonParser.parseString(jsonResponse).getAsJsonObject();

			// 'StatisticSearch'가 없을 경우 안전하게 처리
			JsonObject statisticSearch = jsonObject.getAsJsonObject("StatisticSearch");
			if (statisticSearch == null) {
				continue;
			}

			JsonArray dataArray = statisticSearch.getAsJsonArray("row");
			if (dataArray == null) {
				continue;
			}

			// 날짜와 데이터 값을 처리
			for (int j = 0; j < dataArray.size(); j++) {
				JsonObject data = dataArray.get(j).getAsJsonObject();
				String date = data.get("TIME").getAsString();
				String rate = data.get("DATA_VALUE").getAsString();

				// 각 지표에 맞는 날짜와 값을 추가
				if (i == 0) { // ESI
					esiDates.append("\"").append(date).append("\",");
					esiRates.append(rate).append(",");
				} else if (i == 1) { // CPI
					cpiDates.append("\"").append(date).append("\",");
					cpiRates.append(rate).append(",");
				} else if (i == 2) { // PPI
					ppiDates.append("\"").append(date).append("\",");
					ppiRates.append(rate).append(",");
				}
			}
		}

		// 마지막 쉼표 제거
		if (esiDates.length() > 0)
			esiDates.setLength(esiDates.length() - 1);
		if (esiRates.length() > 0)
			esiRates.setLength(esiRates.length() - 1);

		if (cpiDates.length() > 0)
			cpiDates.setLength(cpiDates.length() - 1);
		if (cpiRates.length() > 0)
			cpiRates.setLength(cpiRates.length() - 1);

		if (ppiDates.length() > 0)
			ppiDates.setLength(ppiDates.length() - 1);
		if (ppiRates.length() > 0)
			ppiRates.setLength(ppiRates.length() - 1);

		// 데이터를 request에 저장

		request.setAttribute("esiDates", "[" + esiDates.toString() + "]");
		request.setAttribute("esiRates", "[" + esiRates.toString() + "]");

		request.setAttribute("cpiDates", "[" + cpiDates.toString() + "]");
		request.setAttribute("cpiRates", "[" + cpiRates.toString() + "]");

		request.setAttribute("ppiDates", "[" + ppiDates.toString() + "]");
		request.setAttribute("ppiRates", "[" + ppiRates.toString() + "]");

		// JSP로 포워딩
		ActionForward forward = new ActionForward();

		HttpSession session = request.getSession();

		// session 객체에 userId가 있으면 /inView.ac로 넘어감

		forward.setRedirect(false);
		forward.setPath("/index3.use");

		return forward;
	}

	// HTTP 요청을 보내고 JSON 응답을 받는 메소드
	private String sendRequest(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

}
