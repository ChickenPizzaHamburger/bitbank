<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사용자 상세 보기</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<c:set var="user" value="${sessionScope.user}" />
<c:set var="accountList" value="${sessionScope.accountList}" />
<div class="container mt-4">
    <h1 class="mb-4">사용자 상세 정보</h1>

    <c:set var="userId" value="${param.userId}" />

    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <h5>사용자 정보</h5>
            <p>유저 ID: <c:out value="${user.userId}" /></p>
            <p>이름: <c:out value="${user.username}" /></p>
            <p>이메일: <c:out value="${user.email}" /></p>
            <p>성별: 
                <c:choose>
                    <c:when test="${user.gender == 'MALE'}">남성</c:when>
                    <c:when test="${user.gender == 'FEMALE'}">여성</c:when>
                    <c:when test="${user.gender == 'ANONY'}">선택안함</c:when>
                </c:choose>
            </p>
            <p>가입일: <c:out value="${user.formattedCreatedAt}" /></p>
        </div>
    </div>

    <h5 class="mb-3">계좌 정보</h5>
    <c:if test="${not empty accountList}">
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>계좌번호</th>
                    <th>계좌 유형</th>
                    <th>잔액</th>
                    <th>개설일</th>
                    <th>액션</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="account" items="${accountList}">
                    <tr>
                        <td><c:out value="${account.account_num}" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${account.account_type == 'COMMON'}">송금 통장</c:when>
                                <c:when test="${account.account_type == 'PLUSBOX'}">플러스박스</c:when>
                                <c:when test="${account.account_type == 'INSTALLMENT'}">적금 통장</c:when>
                            </c:choose>
                        </td>
                        <td><c:out value="${account.account_amount}" /></td>
                        <td><c:out value="${account.account_date}" /></td>
                        <td>
                            <div class="d-flex gap-2">
                                <button class="btn btn-secondary btn-sm" onclick="location.href='accountInfoAction.ac?accountNum=${account.account_num}'">계좌 정보</button>
                                <!-- <button class="btn btn-danger btn-sm" onclick="showAccountActionModal('${account.account_num}', 'delete', '${user.email}')">계좌 삭제</button>  -->
                                <!-- <button class="btn btn-warning btn-sm" onclick="showAccountActionModal('${account.account_num}', 'passwordChange', '${user.email}')">계좌 암호 변경</button>  -->
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${empty accountList}">
        <p>등록된 계좌가 없습니다.</p>
    </c:if>

    <div class="mt-4">
        <button class="btn btn-primary" onclick="history.back()">뒤로가기</button>
    </div>
</div>

<div class="modal fade" id="accountActionModal" tabindex="-1" aria-labelledby="accountActionModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="accountActionModalLabel"></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" id="accountActionModalBody"></div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	function showAccountActionModal(accountNum, action, email) {
	    console.log("showAccountActionModal 호출됨");
	    console.log("accountNum:", accountNum);
	    console.log("action:", action);
	    console.log("email:", email);
	
	    if (action == 'delete') {
	        // 계좌 삭제 모달
	        let deleteModalBody = `
	            <p>계좌 삭제를 위해 인증번호를 입력하세요.</p>
	            <button class="btn btn-primary" id="sendDeleteVerificationBtn">인증번호 전송</button>
	            <div id="deleteVerificationSection" style="display:none; margin-top: 1rem;">
	                <label for="deleteVerificationCode">인증번호</label>
	                <input type="text" id="deleteVerificationCode" class="form-control" placeholder="인증번호 입력" />
	                <button class="btn btn-primary mt-2" onclick="submitVerificationCode('${accountNum}', 'delete')">확인</button>
	            </div>
	        `;
	        
	        $('#accountActionModalLabel').text('계좌 삭제');
	        $('#accountActionModalBody').html(deleteModalBody);
	        $('#accountActionModal').modal('show');
	
	        $('#sendDeleteVerificationBtn').on('click', function() {
	            sendVerificationCode(accountNum, 'delete', email);
	        });
	        
	    } else if (action == 'passwordChange') {
	        // 계좌 암호 변경 모달
	        let passwordChangeModalBody = `
	            <p>계좌 암호 변경을 위해 인증번호를 입력하세요.</p>
	            <button class="btn btn-primary" id="sendPasswordChangeVerificationBtn">인증번호 전송</button>
	            <div id="passwordChangeVerificationSection" style="display:none; margin-top: 1rem;">
	                <label for="passwordChangeVerificationCode">인증번호</label>
	                <input type="text" id="passwordChangeVerificationCode" class="form-control" placeholder="인증번호 입력" />
	                <button class="btn btn-primary mt-2" onclick="submitVerificationCode('${accountNum}', 'passwordChange')">확인</button>
	            </div>
	        `;
	        
	        $('#accountActionModalLabel').text('계좌 암호 변경');
	        $('#accountActionModalBody').html(passwordChangeModalBody);
	        $('#accountActionModal').modal('show');
	
	        $('#sendPasswordChangeVerificationBtn').on('click', function() {
	            sendVerificationCode(accountNum, 'passwordChange', email);
	        });
	    }
	}

	function sendVerificationCode(accountNum, action, email) {
	    console.log("sendVerificationCode 호출됨");

	    $.ajax({
	        url: 'sendCodeAction.use',
	        method: 'POST',
	        data: { accountNum, action, email },
	        success: function(response) {
	            if (response.success) {
	                // 인증번호 전송 후, 인증번호 입력 폼을 보이게 함
	                $(`#${action}VerificationSection`).show();
	            } else {
	                alert('인증번호 전송 실패');
	            }
	        }
	    });
	}

	function submitVerificationCode(accountNum, action) {
	    const verificationCode = $(`#${action}VerificationCode`).val();
	    
	    if (!verificationCode) {
	        alert('인증번호를 입력해주세요.');
	        return;
	    }

	    // action에 따라 다른 URL로 요청 보내기
	    $.ajax({
	        url: action == 'delete' ? 'deleteCodeAction.use' : 'accountPwdChangeCodeAction.use', 
	        method: 'POST',
	        data: { code: verificationCode, accountNum },
	        success: function(response) {
	            if (action == 'delete') {
	                console.log("response.balance:", response.balance);
	            }

	            if (response.success) {
	                renderActionDetails(accountNum, action, response);
	            } else {
	                alert('인증번호 확인 실패. 다시 시도해주세요.');
	            }
	        },
	        error: function() {
	            alert('서버와의 통신에 실패했습니다. 잠시 후 다시 시도해주세요.');
	        }
	    });
	}

	function renderActionDetails(accountNum, action, response) {
	    console.log("renderActionDetails 호출됨");
	    console.log("action:", action);
	    console.log("response:", response);

	    if (action == 'delete') {
	        // balance가 없을 경우 대체 메시지 출력
	        console.log("inner response:", response);
	        const balance = Number(response.balance) || 0;
	        const warningMessage = balance > 0
	            ? `통장 잔액: ${balance}원. 통장에 있는 돈은 은행 계정으로 자동 송금됩니다.`
	            : '통장 잔액이 0원입니다.';

	        const postVerificationSection = `
	            <p>${warningMessage}</p>
	            <p>계좌를 삭제하시겠습니까?</p>
	            <button class="btn btn-danger" onclick="confirmAccountDeletion('${accountNum}', true)">예</button>
	            <button class="btn btn-secondary" onclick="confirmAccountDeletion('${accountNum}', false)">아니요</button>
	        `;

	        // 모달 바디에 내용 삽입
	        $('#accountActionModalBody').html(postVerificationSection); // 모달 내용 직접 업데이트
	    } else {
	        const postVerificationSection = `
	            <label for="newPassword">새 비밀번호</label>
	            <input type="password" id="newPassword" class="form-control" placeholder="새 비밀번호 입력" />
	            <label for="confirmPassword" class="mt-2">비밀번호 확인</label>
	            <input type="password" id="confirmPassword" class="form-control" placeholder="비밀번호 확인 입력" />
	            <button class="btn btn-primary mt-2" onclick="submitNewPassword('${accountNum}')">저장</button>
	        `;

	        // 모달 바디에 내용 삽입
	        $('#accountActionModalBody').html(postVerificationSection); // 모달 내용 직접 업데이트
	    }
	}

	function confirmAccountDeletion(accountNum, approve) {
	    console.log("confirmAccountDeletion 호출됨");
	    console.log("accountNum:", accountNum);
	    console.log("approve:", approve);

	    $.ajax({
	        url: 'deleteAccountAction.use',
	        method: 'POST',
	        data: { accountNum: accountNum, approve: approve.toString() },  // approve를 문자열로 변환
	        success: function(response) {
	            if (response.success) {
	                if (approve === 'true') {
	                    alert('계좌 삭제 완료');
	                } else {
	                    alert('계좌 삭제 취소');
	                }
	                location.reload(); // 페이지 리로드하여 상태 갱신
	            } else {
	                alert('계좌 삭제 실패. 다시 시도해주세요: ' + response.error);
	            }
	        },
	        error: function() {
	            alert('서버와의 통신에 실패했습니다. 잠시 후 다시 시도해주세요.');
	        }
	    });
	}

    function submitNewPassword(accountNum) {
    	console.log("submitNewPassword 호출됨");
	    console.log("accountNum:", accountNum);
    	
        const newPassword = $('#newPassword').val();
        const confirmPassword = $('#confirmPassword').val();

        if (!newPassword || !confirmPassword) {
            alert('모든 필드를 입력해주세요.');
            return;
        }

        if (newPassword != confirmPassword) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        $.ajax({
            url: 'updatePasswordAction.use',
            method: 'POST',
            data: { accountNum, newPassword },
            success: function(response) {
                if (response.success) {
                    alert('비밀번호 변경 완료');
                    location.reload();
                } else {
                    alert('비밀번호 변경 실패');
                }
            }
        });
    }
</script>
</body>
</html>