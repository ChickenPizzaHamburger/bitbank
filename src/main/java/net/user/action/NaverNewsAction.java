package net.user.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import net.util.Action;
import net.util.ActionForward;

public class NaverNewsAction implements Action {
    private static final String clientId = "tjqXvkoloKvYrHHheHvX"; // 발급받은 Client ID
    private static final String clientSecret = "kmCCm_4B_B"; // 발급받은 Client Secret

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] defaultQueries = {"경제", "금융"}; // 기본 검색어
        int display = 5; // 각 검색어당 뉴스 수
        int start = 1;

        // 사용자 정의 검색어 처리 (없으면 기본 검색어 사용)
        String searchQuery = request.getParameter("query");
        String[] queries = (searchQuery != null && !searchQuery.isEmpty())
                ? new String[]{searchQuery}
                : defaultQueries;

        List<Map<String, String>> newsList = new ArrayList<>();

        for (String query : queries) {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String urlStr = "https://openapi.naver.com/v1/search/news.json?query=" + encodedQuery + "&display=" + display + "&start=" + start + "&sort=date";

            try {
                URL url = new URL(urlStr);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                int responseCode = con.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder responseBuffer = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        responseBuffer.append(inputLine);
                    }
                    in.close();

                    JSONObject jsonResponse = new JSONObject(responseBuffer.toString());
                    JSONArray items = jsonResponse.getJSONArray("items");

                    // JSONArray -> List<Map<String, String>> 변환
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        Map<String, String> newsItem = new HashMap<>();
                        newsItem.put("title", item.getString("title"));
                        newsItem.put("link", item.getString("link"));
                        newsItem.put("description", item.getString("description"));
                        newsItem.put("pubDate", item.getString("pubDate"));
                        newsList.add(newsItem);
                    }
                } else {
                    System.err.println("API 요청 실패: 응답 코드 " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 결과를 JSP로 전달
        request.setAttribute("newsList", newsList);  // 뉴스 리스트를 request에 설정
        request.setAttribute("queries", String.join(", ", queries)); // 검색어 표시용

        // JSP로 포워딩
        ActionForward forward = new ActionForward();
        forward.setRedirect(false);  // 포워딩 방식 설정
        forward.setPath("/user/MainView.jsp");  // 포워딩할 JSP 페이지 경로 설정

        return forward;  // 반환
    }
}
