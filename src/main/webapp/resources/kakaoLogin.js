// 카카오 인가 코드 발급을 위한 JavaScript 함수
function requestKakaoLogin() {
    const clientId = '8a86fb5c9d3a0b098175c975919ec566'; // 카카오 앱의 REST API 키
    const redirectUri = encodeURIComponent('http://localhost:8080/BitBank/socialSignupAction.use'); // 인가 코드를 받을 서버 URL (IP주소)
    const responseType = 'code'; // response_type은 항상 'code'로 고정

    // 카카오 로그인 인가 코드 요청 URL
    const authorizationUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=${responseType}`;

    // 사용자가 카카오 로그인 버튼을 클릭하면 해당 URL로 리다이렉션
    window.location.href = authorizationUrl;
}