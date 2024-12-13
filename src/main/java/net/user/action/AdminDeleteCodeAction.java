package net.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import net.account.db.AccountBean;
import net.account.db.AccountDAO;
import net.util.Action;
import net.util.ActionForward;

public class AdminDeleteCodeAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 세션 가져오기
        HttpSession session = request.getSession();

        // 요청에서 파라미터 추출
        String inputCode = request.getParameter("code");
        String inputAccountNum = request.getParameter("accountNum");
        String approveParam = request.getParameter("approve");
        
        // approve 값이 true/false인지 처리
        boolean approve = "true".equalsIgnoreCase(approveParam);

        System.out.println("input Code : " + inputCode);
        System.out.println("input AccountNum : " + inputAccountNum);
        System.out.println("approve : " + approve);

        // 세션에서 저장된 데이터 가져오기
        String storedCode = (String) session.getAttribute("verificationCode");
        String storedAccountNum = (String) session.getAttribute("accountNum");
        String storedAction = (String) session.getAttribute("action");

        System.out.println("stored Code : " + storedCode);
        System.out.println("stored Account Num : " + storedAccountNum);
        System.out.println("stored Action : " + storedAction);

        // JSON 응답 객체 생성
        JSONObject jsonResponse = new JSONObject();

        // 세션 검증
        if (storedCode == null || storedAccountNum == null || storedAction == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "유효하지 않은 세션입니다. 다시 시도해주세요.");
            sendJsonResponse(response, jsonResponse);
            return null;
        }

        // Action 검증
        if (!"delete".equals(storedAction)) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "잘못된 요청입니다. 삭제 작업이 아닙니다.");
            sendJsonResponse(response, jsonResponse);
            return null;
        }

        // 인증번호 및 계좌번호 검증
        if (!storedCode.equals(inputCode) || !storedAccountNum.equals(inputAccountNum)) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "인증번호 또는 계좌번호가 일치하지 않습니다.");
            sendJsonResponse(response, jsonResponse);
            return null;
        }

        // 계좌 잔액 확인 (DB에서 조회)
        int accountBalance = getAccountBalance(inputAccountNum);
        if (accountBalance == -1) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "계좌 정보를 확인할 수 없습니다.");
            sendJsonResponse(response, jsonResponse);
            return null;
        }

        // 계좌 비밀번호 NULL로 변경 (approve가 true일 경우)
//        if (approve) {
//            boolean passwordUpdated = updateAccountPasswordToNull(inputAccountNum);
//            if (!passwordUpdated) {
//                jsonResponse.put("success", false);
//                jsonResponse.put("error", "계좌 비밀번호 업데이트 실패.");
//                sendJsonResponse(response, jsonResponse);
//                return null;
//            }
//        }

        // 성공 응답 (잔액 포함)
        jsonResponse.put("success", true);
        jsonResponse.put("balance", accountBalance);
        sendJsonResponse(response, jsonResponse);

        System.out.println("response : " + response);

        return null;
    }

    // JSON 응답 보내기
    private void sendJsonResponse(HttpServletResponse response, JSONObject jsonResponse) throws Exception {
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }

    // 계좌 잔액 확인 (DB 조회 로직)
    private int getAccountBalance(String accountNum) {
        // AccountDAO 인스턴스 생성
        AccountDAO accountDAO = new AccountDAO();

        // 계좌 정보 조회
        AccountBean account = accountDAO.getAccountInfo(accountNum);

        // 계좌 정보가 없을 경우 -1 반환
        if (account == null) {
            return -1;
        }

        // 계좌 잔액 반환
        return (int) account.getAccount_amount();
    }

    // 계좌 비밀번호를 NULL로 업데이트하는 메서드
//    private boolean updateAccountPasswordToNull(String accountNum) {
//        AccountDAO accountDAO = new AccountDAO();
//        return accountDAO.deactivateAccount(accountNum);
//    }
}
