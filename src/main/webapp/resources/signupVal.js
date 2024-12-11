const uID = document.getElementById('userId');
const uPW = document.getElementById('userPwd');
const uEmail = document.getElementById('userEmail');
const uName = document.getElementById('userName');
const birthDate = document.getElementById('birthDate');

// 에러 메시지 표시 함수
function displayError(elementId, message) {
  const errorElement = document.getElementById(elementId);
  errorElement.textContent = message;
}

// 에러 메시지 제거 함수
function clearError(elementId) {
  const errorElement = document.getElementById(elementId);
  errorElement.textContent = '';
}

// 유효성 검사 함수들
function valid_ID() {
  const idRegExp = /^[a-zA-Z][0-9a-zA-Z]{3,11}$/;
  if (!idRegExp.test(uID.value)) {
    displayError("userIdError", "아이디는 4~12자의 영어 또는 영어+숫자 조합으로만 입력 가능하며, 숫자로 시작할 수 없습니다!");
    uID.classList.add("is-invalid");
    return false;
  }
  clearError("userIdError");
  uID.classList.remove("is-invalid");
  return true;
}

function valid_PW() {
  const pwRegExp = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/;

  if (!pwRegExp.test(uPW.value)) {
    displayError("userPwdError", "비밀번호는 8자 이상의 영어, 숫자, 특수문자를 조합해야 합니다!");
    uPW.classList.add("is-invalid");
    return false;
  }

  if (uPW.value === uID.value) {
    displayError("userPwdError", "비밀번호는 아이디와 동일할 수 없습니다!");
    uPW.classList.add("is-invalid");
    return false;
  }

  if (uPW.value === birthDate.value.replace(/-/g, "")) {
    displayError("userPwdError", "비밀번호는 생년월일과 동일할 수 없습니다!");
    uPW.classList.add("is-invalid");
    return false;
  }

  clearError("userPwdError");
  uPW.classList.remove("is-invalid");
  return true;
}

function valid_EMAIL() {
  const emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,4}$/;
  if (!emailRegExp.test(uEmail.value)) {
    displayError("userEmailError", "이메일 형식을 올바르게 입력하세요! 예: example@domain.com");
    uEmail.classList.add("is-invalid");
    return false;
  }
  clearError("userEmailError");
  uEmail.classList.remove("is-invalid");
  return true;
}

function valid_NAME() {
  const nameRegExp = /^[가-힣]{2,}$/;
  if (!nameRegExp.test(uName.value)) {
    displayError("userNameError", "이름은 한글로 2자 이상 입력해야 합니다!");
    uName.classList.add("is-invalid");
    return false;
  }
  clearError("userNameError");
  uName.classList.remove("is-invalid");
  return true;
}

// 아이디와 이메일 중복 검사 (서버에서 실제 검사를 하기 전에 클라이언트에서 먼저 확인)
function checkDuplication() {
  if (uID.value === uEmail.value) {
    displayError("userEmailError", "아이디와 이메일은 동일할 수 없습니다!");
    uEmail.classList.add("is-invalid");
    return false;
  }
  clearError("userEmailError");
  uEmail.classList.remove("is-invalid");
  return true;
}

// 유효성 검사 후 폼 제출 여부 결정
function valid() {
  const isValidId = valid_ID();   
  const isValidPw = valid_PW();   
  const isValidEmail = valid_EMAIL();   
  const isValidName = valid_NAME();   
  const isValidDuplication = checkDuplication(); // 아이디와 이메일 중복 확인

  return isValidId && isValidPw && isValidEmail && isValidName && isValidDuplication;
}

// 입력 필드에서 blur나 input 이벤트 발생 시 유효성 검사
uID.addEventListener('blur', valid_ID);
uID.addEventListener('input', valid_ID);

uPW.addEventListener('blur', valid_PW);
uPW.addEventListener('input', valid_PW);

uEmail.addEventListener('blur', valid_EMAIL);
uEmail.addEventListener('input', valid_EMAIL);

uName.addEventListener('blur', valid_NAME);
uName.addEventListener('input', valid_NAME);

// 폼 제출 시 유효성 검사
document.getElementById("signupForm").addEventListener("submit", function(event) {
  if (!valid()) {
    event.preventDefault();   // 유효성 검사에 실패하면 폼 제출을 막음
  }
});
