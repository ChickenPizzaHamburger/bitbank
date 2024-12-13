<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>계좌 삭제 인증</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        function verifyDeleteCode(event) {
            event.preventDefault();
            $.ajax({
                url: 'verifyAccountDeleteAction.ac',
                method: 'POST',
                data: $('#verifyForm').serialize(),
                success: function(response) {
                    if (response.success) {
                        alert('계좌 삭제 완료.');
                        location.href = 'accountListView.ac';
                    } else {
                        alert(response.error);
                    }
                },
                error: function(xhr, status, error) {
                    alert('서버 오류가 발생했습니다. 다시 시도해주세요.');
                }
            });
        }
    </script>
</head>
<body>
    <div class="container d-flex justify-content-center align-items-center min-vh-100">
        <div class="card p-4" style="width: 400px;">
            <h3 class="text-center">계좌 삭제 인증</h3>
            <form id="verifyForm">
                <input type="hidden" name="accountNum" value="${accountNum}">
                <div class="mb-3">
                    <label for="code" class="form-label">인증번호</label>
                    <input type="text" class="form-control" id="code" name="code" required>
                </div>
                <button class="btn btn-danger w-100" onclick="verifyDeleteCode(event);">삭제 확인</button>
            </form>
        </div>
    </div>
</body>
</html>