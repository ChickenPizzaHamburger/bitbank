package net.user.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import net.user.db.UserBean;
import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;

public class SocialSignupAction implements Action { // 소셜 회원가입 및 로그인

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 클라이언트가 리다이렉트된 후 인가 코드 받기
        String code = request.getParameter("code");
        
        System.out.println("code : " + code);

        if (code == null) {
            // 에러 처리 (인가 코드가 없다면)
            response.getWriter().write("Error: Missing authorization code.");
            return null;
        }

        // 토큰 발급을 위한 요청 파라미터 설정
        String grantType = "authorization_code"; // 고정값
        String clientId = "8a86fb5c9d3a0b098175c975919ec566"; // 앱 REST API 키
        String clientSecret = "Nz3axD0m8YeVvsny72hrem0b3N5O0kX1"; // 클라이언트 시크릿
        String redirectUri = "http://localhost:8080/BitBank/socialSignupAction.use"; // 리다이렉트 URI (IP주소)

        // 카카오 토큰 발급 API 호출 URL
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        // POST 요청을 위한 파라미터 설정
        String parameters = "grant_type=" + grantType + "&"
                            + "client_id=" + clientId + "&"
                            + "redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) + "&"
                            + "code=" + code + "&"
                            + "client_secret=" + clientSecret;

        // 토큰 발급 요청을 보내는 HttpURLConnection 설정
        URL url = new URL(tokenUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        // 요청 본문에 파라미터를 작성
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = parameters.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 응답 받기
        int responseCode = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            responseBuffer.append(inputLine);
        }
        in.close();

        // 응답 처리 (JSON 파싱)
        String responseStr = responseBuffer.toString();
        JSONObject jsonResponse = new JSONObject(responseStr);

        // 액세스 토큰 및 리프레시 토큰 추출
        String accessToken = jsonResponse.optString("access_token");
        String refreshToken = jsonResponse.optString("refresh_token");

        // 토큰 정보 출력 (디버깅용)
        System.out.println("Access Token: " + accessToken);
        System.out.println("Refresh Token: " + refreshToken);

        // 카카오 API에서 사용자 정보 가져오기
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        
        // 사용자 정보를 요청할 때 액세스 토큰을 헤더에 담아서 요청
        URL userInfoRequestUrl = new URL(userInfoUrl);
        HttpURLConnection userInfoConnection = (HttpURLConnection) userInfoRequestUrl.openConnection();
        userInfoConnection.setRequestMethod("GET");
        userInfoConnection.setRequestProperty("Authorization", "Bearer " + accessToken);  // Authorization 헤더에 액세스 토큰 추가
        userInfoConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        
        // 응답 받기
        BufferedReader userInfoIn = new BufferedReader(new InputStreamReader(userInfoConnection.getInputStream()));
        StringBuffer userInfoResponseBuffer = new StringBuffer();
        while ((inputLine = userInfoIn.readLine()) != null) {
            userInfoResponseBuffer.append(inputLine);
        }
        userInfoIn.close();
        
        // 응답 처리 (사용자 정보 파싱)
        String userInfoResponseStr = userInfoResponseBuffer.toString();
        JSONObject userInfoResponseJson = new JSONObject(userInfoResponseStr);
        
        // 사용자 정보 추출
        long userId = userInfoResponseJson.getLong("id");
        JSONObject kakaoAccount = userInfoResponseJson.getJSONObject("kakao_account");
        String userName = kakaoAccount.optString("name");
        String email = kakaoAccount.optString("email");
        String birthYear = kakaoAccount.optString("birthyear");
        String birthDay = kakaoAccount.optString("birthday");
        String gender = kakaoAccount.optString("gender");
        
        // birthYear와 birthDay를 결합하여 birthDate 형식(YYYY-MM-DD)으로 만들기
        String birthDate = null;
        if (birthYear != null && birthDay != null) {
            birthDate = birthYear + "-" + birthDay.substring(0, 2) + "-" + birthDay.substring(2, 4);
        }
        
        // 사용자 정보 출력 (디버깅용)
        System.out.println("User ID: " + userId);
        System.out.println("User Name: " + userName);
        System.out.println("Email: " + email);
        System.out.println("birthDate: " + birthDate);
        System.out.println("Gender: " + gender);
        
        // 데이터베이스에 사용자 정보 저장 (UserBean 및 UserDAO 사용)
        UserDAO userDAO = new UserDAO();
        boolean isExistingUser = userDAO.checkUserId("$kakao$" + userId); // 기존 사용자 확인
        
        UserBean user = new UserBean();
        user.setUserId("$kakao$" + String.valueOf(userId));  // 카카오에서 제공하는 ID는 long이므로 String으로 변환
        user.setUsername(userName);
        user.setEmail("$" + email);
        user.setBirthDate(birthDate != null ? LocalDate.parse(birthDate) : null);
        user.setGender(UserBean.Gender.valueOf(gender.toUpperCase()));
        user.setRole(UserBean.Role.USER); // 기본 역할은 USER
        
        // 로그인 후 원하는 페이지로 리다이렉트
        ActionForward forward = new ActionForward();
        if (isExistingUser) {
            // 이미 존재하는 사용자라면 소셜 로그인 처리
        	request.setAttribute("userId", "$kakao$" + String.valueOf(userId));  // 로그인한 사용자 정보 세션에 저장
            System.out.println("소셜 로그인 성공! 세션에 저장된 userId: " + user.getUserId());
            forward.setRedirect(false);
            forward.setPath("./loginAction.use");  //로그인 처리
        } else {
            // 새 사용자라면 회원가입 처리
            boolean isRegistered = userDAO.signup(user);
            if (isRegistered) {
            	request.setAttribute("userId", "$kakao$" + String.valueOf(userId));  // 회원가입 후 세션에 사용자 정보 저장
                System.out.println("소셜 로그인 성공! 세션에 저장된 userId: " + user.getUserId());
                forward.setRedirect(false);
                forward.setPath("./loginAction.use");  // 로그인 처리
            } else {
                request.setAttribute("error", "회원가입에 실패했습니다. 다시 시도해주세요.");
                forward.setPath("./loginView.use");  // 다시 폼 페이지로 이동
                forward.setRedirect(true);
            }
        }

        return forward;
    }
}