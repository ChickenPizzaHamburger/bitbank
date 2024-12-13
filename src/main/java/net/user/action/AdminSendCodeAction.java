package net.user.action;

import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import net.util.Action;
import net.util.ActionForward;

public class AdminSendCodeAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();
        JSONObject jsonResponse = new JSONObject();

        if ("POST".equalsIgnoreCase(method)) {
            // 파라미터 추출
            String accountNum = request.getParameter("accountNum");
            String action = request.getParameter("action");
            String email = request.getParameter("email");
            
            System.out.println("어카운트 : " + accountNum);
            System.out.println("액션 : " + action);
            System.out.println("이메일 : " + email);

            // 세션에서 사용자 정보 가져오기
            HttpSession session = request.getSession();

            // 기존 인증번호 폐기
            resetSessionAttributes(session);

            // 인증번호 생성 및 저장
            String verificationCode = generateVerificationCode();
            saveVerificationCodeToSession(session, verificationCode, accountNum, action);

            // 이메일 전송
            boolean emailSent = sendEmail(email, verificationCode, action);

            if (emailSent) {
                jsonResponse.put("success", true);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("error", "이메일 전송에 실패했습니다. 다시 시도해주세요.");
            }

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "허용되지 않는 요청 방식입니다.");
        }

        return null;
    }

    // 세션 속성 초기화
    private void resetSessionAttributes(HttpSession session) {
        session.removeAttribute("verificationCode");
        session.removeAttribute("accountNum");
        session.removeAttribute("action");
    }

    // 인증번호 생성 및 세션 저장
    private void saveVerificationCodeToSession(HttpSession session, String verificationCode, String accountNum, String action) {
        session.setAttribute("verificationCode", verificationCode);
        session.setAttribute("accountNum", accountNum);
        session.setAttribute("action", action);
    }

    // 인증번호 생성
    private String generateVerificationCode() {
        SecureRandom secureRand = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(secureRand.nextInt(10));
        }
        return code.toString();
    }

    // 이메일 전송 메서드
    private boolean sendEmail(String recipientEmail, String verificationCode, String action) {
        String subject = null;
        String messageBody = null;

        // Action 값에 따라 제목 및 본문 설정
        switch (action) {
            case "delete":
                subject = "BitBank 계좌 삭제 코드 요청";
                messageBody = "당신의 계좌 삭제 인증번호는 " + verificationCode + "입니다.";
                break;
            case "passwordChange":
                subject = "BitBank 계좌 비밀번호 변경 코드 요청";
                messageBody = "당신의 계좌 비밀번호 변경 인증번호는 " + verificationCode + "입니다.";
                break;
        }

        String user = "bit.bank.pwd@gmail.com";
        String password = "baqd jtdx nrmz osvz";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);
            System.out.println("이메일이 성공적으로 전송되었습니다.");
            return true;
        } catch (MessagingException e) {
            System.err.println("이메일 전송에 문제가 생겼습니다. : " + e);
            return false;
        }
    }
}