<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>전체 유저 보기</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- 부트스트랩 아이콘 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	rel="stylesheet">
</head>
<body>
	<%
	// 로그인 상태 확인
	String userId = (String) session.getAttribute("userId");
	String baseURL = "http://localhost:8080/BitBank(1211ver2)";
	String targetPage = userId != null ? baseURL + "/accountView.ac" : baseURL + "/loginView.use";
	%>
	<script>
        // DOM이 로드된 후 이벤트를 등록합니다.
        document.addEventListener("DOMContentLoaded", () => {
            const loginBtn = document.getElementById("login-btn");
            const signupBtn = document.getElementById("signup-btn");
            const logoutBtn = document.getElementById("logout-btn");
            
            loginBtn.addEventListener("click", () => {
            	location.href = "<%=baseURL%>/index.use";
            });
            
          		
            //logout 버튼을 눌렀을 때 동작
            logoutBtn.addEventListener("click", () => {
            	invalidateSession();
            	location.href = "<%=baseURL%>/index.use";
            });
        });
        function invalidateSession() {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "LogoutAction.use", true); // invalidateSession은 세션을 초기화하는 서블릿 경로
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    // 세션이 무효화된 후 처리할 코드
                    alert("로그아웃되었습니다.");
                }
            };
            xhr.send();
        }
        
        
    </script>
	<header class="bg-primary py-3">
		<div
			class="container d-flex justify-content-between align-items-center">
			<a href="index.use"
				class="text-white text-decoration-none d-flex align-items-center">
				<i class="bi bi-bank fs-3"></i> <span class="ms-2 fw-bold">BitBank</span>
			</a>
			<nav>
				<div>
					<button class="btn btn-light me-2" id="login-btn"
						style="<%=userId == null ? "" : "display: none;"%>">Login</button>
					<button class="btn btn-light" id="logout-btn"
						style="<%=userId != null ? "" : "display: none;"%>">Logout</button>
				</div>
			</nav>
		</div>
	</header>
	<div class="container">
		<h1>전체 유저 목록</h1>
		<form method="get" action="userListAction.use" class="mb-3">
			<input type="hidden" name="command" value="viewUserList">
			<div class="input-group">
				<input type="text" name="searchKeyword" class="form-control"
					placeholder="검색어 입력" value="${param.searchKeyword}"
					style="width: 50%;"> <input type="date" name="joinDate"
					class="form-control" placeholder="가입일 입력" value="${param.joinDate}"
					style="width: 20%;" min="2024-01-01"
					max="<%= java.time.LocalDate.now() %>">
				<button type="submit" class="btn btn-primary">검색</button>
				<button type="button" class="btn btn-secondary"
					onclick="location.href='userListAction.use?command=viewUserList'">전체
					보기</button>
			</div>
		</form>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>유저 ID</th>
					<th>이름</th>
					<th>이메일</th>
					<th>성별</th>
					<th>가입일</th>
					<th>액션</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${sessionScope.userList}">
					<tr>
						<td>${user.userId}</td>
						<td>${user.username}</td>
						<td>${user.email}</td>
						<td><c:choose>
								<c:when test="${user.gender == 'MALE'}">남성</c:when>
								<c:when test="${user.gender == 'FEMALE'}">여성</c:when>
								<c:when test="${user.gender == 'ANONY'}">선택안함</c:when>
							</c:choose></td>
						<td>${user.formattedCreatedAt}</td>
						<td>
							<div class="d-flex gap-2">
								<a href="userInfoAction.use?userId=${user.userId}"
									class="btn btn-info">상세 보기</a>

								<c:if test="${user.role == 'USER'}">
									<!-- 관리자 임명 버튼 -->
									<button class="btn btn-success"
										onclick="showAdminAppointmentSection('${user.userId}');">관리자
										임명</button>
								</c:if>

								<c:if
									test="${user.role == 'ADMIN' && user.userId != sessionScope.userId}">
									<!-- 관리자 해제 버튼 (자기 자신이 아닌 경우만) -->
									<button class="btn btn-warning"
										onclick="showAdminManagementSection('${user.userId}');">관리자
										해제</button>
								</c:if>
							</div>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<nav aria-label="Page navigation">
			<ul class="pagination justify-content-center">
				<c:if test="${sessionScope.currentPage > 1}">
					<li class="page-item"><a class="page-link"
						href="userListAction.use?command=viewUserList&page=${sessionScope.currentPage - 1}&searchKeyword=${sessionScope.searchKeyword}&joinDate=${sessionScope.joinDate}"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<c:forEach var="page" begin="1" end="${sessionScope.totalPages}">
					<li
						class="page-item ${page == sessionScope.currentPage ? 'active' : ''}">
						<a class="page-link"
						href="userListAction.use?command=viewUserList&page=${page}&searchKeyword=${sessionScope.searchKeyword}&joinDate=${sessionScope.joinDate}">${page}</a>
					</li>
				</c:forEach>
				<c:if test="${sessionScope.currentPage < sessionScope.totalPages}">
					<li class="page-item"><a class="page-link"
						href="userListAction.use?command=viewUserList&page=${sessionScope.currentPage + 1}&searchKeyword=${sessionScope.searchKeyword}&joinDate=${sessionScope.joinDate}"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
        function showAdminAppointmentSection(userId) {
            if (confirm("이 사용자를 관리자로 임명하시겠습니까?")) {
                changeUserRole(userId, 'ADMIN');
            }
        }

        function showAdminManagementSection(userId) {
            if (confirm("이 사용자의 관리자 권한을 해제하시겠습니까?")) {
                changeUserRole(userId, 'USER');
            }
        }

        function changeUserRole(userId, newRole) {
            $.ajax({
                url: 'changeUserRoleAction.use',
                method: 'POST',
                data: { userId: userId, newRole: newRole },
                success: function(response) {
                    if (response.success) {
                        alert('권한이 성공적으로 변경되었습니다.');
                        // 권한 변경 후 전체 유저 보기 페이지로 리다이렉트
                        window.location.href = 'userListAction.use?command=viewUserList';
                    } else {
                        alert('권한 변경에 실패했습니다.');
                    }
                },
                error: function() {
                    alert('서버 오류가 발생했습니다.');
                }
            });
        }
    </script>
</body>
</html>