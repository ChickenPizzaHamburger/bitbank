<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>송금 금액 입력</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body, html {
            height: 100%;
            margin: 0;
            background-color: #f7f7f7;
        }

        .full-height {
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: center;
        }

        .container {
            background-color: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            width: 100%;
        }

        .warning-text {
            color: red;
            font-weight: bold;
        }

        .btn-disabled {
            background-color: #b0c4de;
            cursor: not-allowed;
        }

        .form-label {
            font-size: 1.2rem;
            text-align: center;
            display: block;
        }

        .mb-3 {
            text-align: left;
        }

        .center-text {
            text-align: center;
        }

        /* 금액 초과 메시지 중앙 정렬 */
        #warningMessage {
            display: block;
            margin: 0 auto;
            text-align: center;
            font-size: 1.1rem;
        }

        /* 송금 금액 뒤 원 표시 */
        #amount {
            width: calc(100% - 50px);
        }
        .amount-container {
            display: flex;
            align-items: center;
        }
        .amount-container span {
            font-size: 1.2rem;
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <div class="full-height">
        <div class="container shadow-lg">
            <h1 class="text-center mb-4">송금 금액 입력</h1>
            <form action="submitTransferAction.tr" method="post" id="transferForm">
                <!-- 송금 금액 입력란 -->
                <div class="mb-3">
                    <label for="amount" class="form-label">얼마를 보내시겠어요?</label>
                    <div class="amount-container">
                        <input type="text" id="amount" name="amount" class="form-control" min="1" required>
                        <span>원</span>
                    </div>
                </div>

                <!-- 금액 초과 여부 -->
                <div class="mb-3">
                    <span id="warningMessage" class="warning-text"></span>
                </div>

                <!-- 출금 가능 금액 -->
                <div class="mb-3 center-text">
                    <span>출금 가능 금액: <span id="availableAmount">0</span> 원</span>
                </div>

                <!-- 송금 수수료 -->
                <div class="mb-3 center-text">
                    <span>송금 수수료: 500 원</span>
                </div>

                <!-- 송금 금액 + 수수료 계산 -->
                <div class="mb-3 center-text">
                    <span>총 송금 금액 (수수료 포함): <span id="totalAmount">0</span> 원</span>
                </div>

                <!-- 태그 입력 -->
                <div class="mb-3">
                    <label for="tag" class="form-label">태그 입력</label>
                    <input type="text" id="tag" name="tag" class="form-control">
                </div>

                <!-- 확인 버튼 -->
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary btn-lg" id="confirmButton" disabled>확인</button>
                </div>
            </form>
        </div>
    </div>

    <!-- 비밀번호 입력 팝업 -->
    <div class="modal fade" id="passwordModal" tabindex="-1" aria-labelledby="passwordModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="passwordModalLabel">비밀번호 입력</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <label for="password">비밀번호:</label>
                    <input type="password" id="password" class="form-control" required>
                    <div class="d-grid gap-2 mt-3">
                        <button type="button" class="btn btn-primary" onclick="submitTransfer()">확인</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        document.getElementById('amount').addEventListener('input', function () {
            // 1. 입력된 값에서 숫자만 추출
            let amount = this.value.replace(/[^0-9]/g, ''); // 숫자만 추출

            // 2. 3자리마다 쉼표 추가
            let formattedAmount = amount.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

            // 3. 입력 필드에 다시 쉼표가 추가된 값 설정
            this.value = formattedAmount;

            // 4. 입력값에서 쉼표를 제외한 숫자만 사용하여 계산
            let enteredAmount = parseInt(amount); // 쉼표를 제거한 숫자만 사용
            let availableAmount = 1000000; // 예시: 출금 가능 금액 1,000,000 원
            let fee = 500;
            let totalAmount = enteredAmount + fee;

            // 5. 송금 금액이 출금 가능 금액을 초과하면 경고 메시지 표시
            let warningMessage = '';
            let isDisabled = false;
            if (enteredAmount + fee > availableAmount) {
                warningMessage = '출금 금액이 부족합니다.';
                document.getElementById('warningMessage').innerText = warningMessage;
                isDisabled = true;
                document.getElementById('confirmButton').classList.add('btn-disabled');
            } else {
                document.getElementById('warningMessage').innerText = '';
                isDisabled = false;
                document.getElementById('confirmButton').classList.remove('btn-disabled');
            }

            // 6. 총 송금 금액과 원화 변환
            document.getElementById('availableAmount').innerText = availableAmount.toLocaleString();
            document.getElementById('totalAmount').innerText = totalAmount.toLocaleString();
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
