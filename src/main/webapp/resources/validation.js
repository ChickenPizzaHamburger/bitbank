let uID = document.getElementById("userId");
let uPW = document.getElementById("userPwd");
let uEmail = document.getElementById("userEmail");

function valid_ID() {
    const idRegExp = RegExp(/^[0-9a-zA-Z]{4,12}$/);
    if (uID.value === '' || !idRegExp.test(uID.value)) {
        alert('아이디를 4-12자 사이 영문자와 숫자 조합으로만 입력하세요!');
        uID.focus();
        return false;
    }
    return true;
}

function valid_PW() {
    const pwRegExp = RegExp(/^[0-9a-zA-Z]{4,12}$/);
    if (!pwRegExp.test(uPW.value)) {
        alert('비밀번호를 4-12자 사이 영문자와 숫자 조합으로만 입력하세요!');
        uPW.focus();
        return false;
    }
    return true;
}

function valid_EMAIL() {
    const emailRegExp = RegExp(/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i);
    if (!emailRegExp.test(uEmail.value)) {
        alert('이메일 형식을 갖추어서 입력하세요!');
        uEmail.focus();
        return false;
    }
    return true;
}

function valid() {
    let ID = valid_ID();
    let PW = valid_PW();
    let Email = valid_EMAIL();

    if (ID && PW && Email) {
        alert('정상적으로 입력이 완료되었습니다!');
        return true;
    } else {
        return false;
    }
}
