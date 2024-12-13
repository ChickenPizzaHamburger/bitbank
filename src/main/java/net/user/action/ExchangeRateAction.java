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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.util.Action;
import net.util.ActionForward;

public class ExchangeRateAction implements Action {

	private static final String API_KEY = "AIAJ3DPRYSGSRCMO6E29"; // 발급받은 한국은행 API 키
	private static final String API_URL = "https://ecos.bok.or.kr/api/StatisticSearch/";

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		LocalDate endDate = LocalDate.now(); // 오늘 날짜
		LocalDate startDate = endDate.minusMonths(1); // 한 달 전 날짜

		// 날짜 포맷 설정 (yyyyMMdd)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String startDateStr = startDate.format(formatter);
		String endDateStr = endDate.format(formatter);

		String[] currencies = { "0000001", "0000002", "0000003" }; // 달러, 엔화, 유로
		String[] currencyNames = { "USD", "JPY", "EUR" };

		StringBuilder usdDates = new StringBuilder();
		StringBuilder usdRates = new StringBuilder();

		StringBuilder jpyDates = new StringBuilder();
		StringBuilder jpyRates = new StringBuilder();

		StringBuilder eurDates = new StringBuilder();
		StringBuilder eurRates = new StringBuilder();

		for (int i = 0; i < currencies.length; i++) {
			String currency = currencies[i];
			String urlString = String.format("%s%s/json/kr/1/100/731Y001/D/%s/%s/%s", API_URL, API_KEY, startDateStr,
					endDateStr, currency);

			
			// API 요청
			String jsonResponse = sendRequest(urlString);
			
			// JSON 응답 파싱
			JsonObject jsonObject = com.google.gson.JsonParser.parseString(jsonResponse).getAsJsonObject();
			JsonArray dataArray = jsonObject.getAsJsonObject("StatisticSearch").getAsJsonArray("row");

//        // 날짜와 환율 값을 배열로 전달
//        StringBuilder dates = new StringBuilder();
//        StringBuilder rates = new StringBuilder();

			for (int j = 0; j < dataArray.size(); j++) {
				JsonObject data = dataArray.get(j).getAsJsonObject();
				String date = data.get("TIME").getAsString();
				String rate = data.get("DATA_VALUE").getAsString();

				if (i == 0) { // USD
					usdDates.append("\"").append(date).append("\",");
					usdRates.append(rate).append(",");
				} else if (i == 1) { // JPY
					jpyDates.append("\"").append(date).append("\",");
					jpyRates.append(rate).append(",");
				} else if (i == 2) { // EUR
					eurDates.append("\"").append(date).append("\",");
					eurRates.append(rate).append(",");
				}
			}
		}
		// 마지막 쉼표 제거
		if (usdDates.length() > 0)
			usdDates.setLength(usdDates.length() - 1);
		if (usdRates.length() > 0)
			usdRates.setLength(usdRates.length() - 1);

		if (jpyDates.length() > 0)
			jpyDates.setLength(jpyDates.length() - 1);
		if (jpyRates.length() > 0)
			jpyRates.setLength(jpyRates.length() - 1);

		if (eurDates.length() > 0)
			eurDates.setLength(eurDates.length() - 1);
		if (eurRates.length() > 0)
			eurRates.setLength(eurRates.length() - 1);

		// 데이터를 request에 저장
		request.setAttribute("usdDates", "[" + usdDates.toString() + "]");
		request.setAttribute("usdRates", "[" + usdRates.toString() + "]");

		request.setAttribute("jpyDates", "[" + jpyDates.toString() + "]");
		request.setAttribute("jpyRates", "[" + jpyRates.toString() + "]");

		request.setAttribute("eurDates", "[" + eurDates.toString() + "]");
		request.setAttribute("eurRates", "[" + eurRates.toString() + "]");

		// JSP로 포워딩
		ActionForward forward = new ActionForward();
        forward.setPath("/index2.use"); // 다시 폼 페이지로 이동
        forward.setRedirect(false);
        
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
