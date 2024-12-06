<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>계좌이체</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600&display=swap" rel="stylesheet">
    <style>
        body {
            background-color: #f7f7f7;
            font-family: 'Poppins', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        h1 {
            font-weight: 600;
            margin-bottom: 30px;
        }

        .card {
            border-radius: 10px;
            cursor: pointer;
        }

        .btn-select {
            border: 1px solid #ddd;
            padding: 10px 20px;
            border-radius: 10px;
            font-size: 1rem;
            background-color: #fff;
            font-weight: 500;
            cursor: pointer;
            transition: 0.3s;
        }

        .btn-select.active {
            background-color: #4A90E2;
            color: #fff;
            font-weight: 600;
        }

        .btn-primary {
            background-color: #4A90E2;
            border: none;
        }

        .btn-primary:hover {
            background-color: #357ab7;
        }

        .form-label {
            font-size: 1.2rem;
            font-weight: 500;
            color: #333;
        }

        .container {
            background-color: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="text-center">계좌이체</h1>

        <!-- 계좌 카드 -->
        <div class="card mb-4" id="accountCard">
            <div class="card-header d-flex align-items-center">
                <img src="bank-logo.png" alt="Bank Logo" class="rounded-circle me-3" style="width: 50px; height: 50px;">
                <div>
                    <div>BitBank - ${account.accountType}</div>
                    <div>${account.accountNumber}</div>
                </div>
                <div class="ms-auto text-end">
                    <div>출금 가능 금액: ${account.accountAmount}원</div>
                    <div>1일 송금 잔여 한도: ${dailyLimit}원</div>
                </div>
            </div>
        </div>

        <!-- 송금 폼 -->
        <form action="sendAmountView.jsp" method="get">
            <!-- 은행명 고정 -->
            <div class="mb-3">
                <label for="bankName" class="form-label">은행명</label>
                <input type="text" id="bankName" name="bankName" class="form-control" value="BitBank" readonly>
            </div>

            <!-- 받는 계좌 -->
            <div class="mb-3">
                <label for="receiverAccount" class="form-label">받는 계좌</label>
                <input type="text" id="receiverAccount" name="receiverAccount" class="form-control" required readonly>
            </div>

            <!-- 선택 버튼 -->
            <div class="d-flex justify-content-center gap-3 mb-4">
                <button type="button" class="btn-select active" id="recentBtn" data-target="recent">최근 송금</button>
                <button type="button" class="btn-select" id="myAccountsBtn" data-target="myAccounts">내 계좌</button>
                <button type="button" class="btn-select" id="favoritesBtn" data-target="favorites">자주 쓰는 계좌</button>
            </div>

            <!-- 선택한 내용 -->
            <div id="recent" class="tab-content active">
                <label class="form-label">최근 송금 내역</label>
                <div class="d-flex flex-wrap gap-2">
                    <c:forEach var="recentAccount" items="${recentAccounts}">
                        <button type="button" class="btn btn-outline-secondary" onclick="selectAccount('${recentAccount.accountNumber}')">
                            ${recentAccount.accountName} (${recentAccount.accountNumber})
                        </button>
                    </c:forEach>
                </div>
            </div>

            <div id="myAccounts" class="tab-content" style="display: none;">
                <label class="form-label">내 계좌 목록</label>
                <div class="d-flex flex-wrap gap-2">
                    <c:forEach var="myAccount" items="${myAccounts}">
                        <button type="button" class="btn btn-outline-secondary" onclick="selectAccount('${myAccount.accountNumber}')">
                            ${myAccount.accountName} (${myAccount.accountNumber})
                        </button>
                    </c:forEach>
                </div>
            </div>

            <div id="favorites" class="tab-content" style="display: none;">
                <label class="form-label">자주 쓰는 계좌</label>
                <div class="d-flex flex-wrap gap-2">
                    <c:forEach var="favoriteAccount" items="${favoriteAccounts}">
                        <button type="button" class="btn btn-outline-secondary" onclick="selectAccount('${favoriteAccount.accountNumber}')">
                            ${favoriteAccount.accountName} (${favoriteAccount.accountNumber})
                        </button>
                    </c:forEach>
                </div>
            </div>

            <!-- 확인 버튼 -->
            <div class="d-grid mt-4">
                <button type="submit" class="btn btn-primary btn-lg">확인</button>
            </div>
        </form>
    </div>

    <!-- 계좌 선택 모달 -->
    <div class="modal fade" id="accountModal" tabindex="-1" aria-labelledby="accountModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="accountModalLabel">계좌 선택</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div id="recent" class="tab-content active">
                        <label class="form-label">최근 송금 내역</label>
                        <div class="d-flex flex-wrap gap-2">
                            <c:forEach var="recentAccount" items="${recentAccounts}">
                                <button type="button" class="btn btn-outline-secondary" onclick="selectAccount('${recentAccount.accountNumber}')">
                                    ${recentAccount.accountName} (${recentAccount.accountNumber})
                                </button>
                            </c:forEach>
                        </div>
                    </div>

                    <div id="myAccounts" class="tab-content" style="display: none;">
                        <label class="form-label">내 계좌 목록</label>
                        <div class="d-flex flex-wrap gap-2">
                            <c:forEach var="myAccount" items="${myAccounts}">
                                <button type="button" class="btn btn-outline-secondary" onclick="selectAccount('${myAccount.accountNumber}')">
                                    ${myAccount.accountName} (${myAccount.accountNumber})
                                </button>
                            </c:forEach>
                        </div>
                    </div>

                    <div id="favorites" class="tab-content" style="display: none;">
                        <label class="form-label">자주 쓰는 계좌</label>
                        <div class="d-flex flex-wrap gap-2">
                            <c:forEach var="favoriteAccount" items="${favoriteAccounts}">
                                <button type="button" class="btn btn-outline-secondary" onclick="selectAccount('${favoriteAccount.accountNumber}')">
                                    ${favoriteAccount.accountName} (${favoriteAccount.accountNumber})
                                </button>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        const accountCard = document.getElementById('accountCard');
        accountCard.addEventListener('click', () => {
            const modal = new bootstrap.Modal(document.getElementById('accountModal'));
            modal.show();
        });

        const buttons = document.querySelectorAll('.btn-select');
        const tabs = document.querySelectorAll('.tab-content');

        buttons.forEach(button => {
            button.addEventListener('click', () => {
                buttons.forEach(btn => btn.classList.remove('active'));
                tabs.forEach(tab => tab.style.display = 'none');

                button.classList.add('active');
                document.getElementById(button.dataset.target).style.display = 'block';
            });
        });

        function selectAccount(accountNumber) {
            document.getElementById('receiverAccount').value = accountNumber;
            const modal = new bootstrap.Modal(document.getElementById('accountModal'));
            modal.hide();
        }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
