/**
 * 로그인 방식에 따라 적절한 섹션을 표시하는 함수
 * @param {string} type - 선택된 로그인 유형 ('default', 'social', 'qr')
 */
function toggleLoginForm(type) {
    // 모든 섹션을 숨김
    document.querySelectorAll('.login-section').forEach(section => {
        section.style.display = 'none';
    });

    // 링크 영역 가져오기
    const links = document.getElementById('login-links');

    // 선택된 섹션만 표시
    if (type === 'default') {
        document.getElementById('default-login').style.display = 'block';
        links.style.display = 'block'; // 링크 표시
    } else if (type === 'social') {
        document.getElementById('social-login').style.display = 'block';
        links.style.display = 'none'; // 링크 숨김
    } else if (type === 'qr') {
        document.getElementById('qr-login').style.display = 'block';
        links.style.display = 'block'; // 링크 표시
    }
}
