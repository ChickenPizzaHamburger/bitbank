<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>송금 완료</title>
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

        .completed-message {
            font-size: 1.4rem;
            font-weight: bold;
            color: #28a745;
        }

        .error-message {
            color: red;
            font-weight: bold;
        }

        .back-button {
            margin-top: 30px;
        }

    </style>
</head>
<body>
    <div class="full-height">
        <div class="container shadow-lg">
            <h1 class="text-center mb-4">송금 결과</h1>

            <!-- 송금 성공 여부에 따른 메시지 -->
            <c:choose>
                <c:when test="${transferSuccess}">
                    <div class="completed-message">
                        송금이 성공적으로 완료되었습니다!
                    </div>

                    <!-- 송금 내역 -->
                    <div class="mb-3">
                        <div><strong>보내는 계좌:</strong> ${senderAccount}</div>
                        <div><strong>받는 계좌:</strong> ${receiverAccount}</div>
                        <div><strong>송금 금액:</strong> ${amount} 원</div>
                        <div><strong>태그:</strong> ${tag}</div>
                        <div><strong>송금 시간:</strong> ${transferTime}</div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="error-message">
                        송금에 실패했습니다. ${errorMessage}
                    </div>
                </c:otherwise>
            </c:choose>

            <!-- 돌아가기 버튼 -->
            <div class="d-grid gap-2 back-button">
                <a href="accountAction.ac" class="btn btn-success btn-lg">계좌 목록으로 돌아가기</a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>