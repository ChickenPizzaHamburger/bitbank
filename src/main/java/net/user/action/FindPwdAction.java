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

import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;

public class FindPwdAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();
        JSONObject jsonResponse = new JSONObject();

        if ("POST".equalsIgnoreCase(method)) {
            String userId = request.getParameter("id");
            String userEmail = request.getParameter("email");

            UserDAO dao = new UserDAO();
            boolean isValid = dao.checkUserEmail(userId, userEmail);

            HttpSession session = request.getSession();
            Long expirationTime = (Long) session.getAttribute("expirationTime");

            if (expirationTime != null && System.currentTimeMillis() > expirationTime) {
                session.removeAttribute("verificationCode");
                session.removeAttribute("expirationTime");
                expirationTime = null;
            }

            if (isValid) {
                String verificationCode = generateVerificationCode();
                boolean emailSent = sendEmail(userEmail, verificationCode);
                if (emailSent) {
                    expirationTime = System.currentTimeMillis() + 3 * 60 * 1000;
                    session.setAttribute("userId", userId);
                    session.setAttribute("verificationCode", verificationCode);
                    session.setAttribute("expirationTime", expirationTime);

                    jsonResponse.put("success", true);
                    response.setContentType("application/json");
                    response.getWriter().write(jsonResponse.toString());
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("error", "이메일 전송에 실패했습니다. 다시 시도해주세요.");
                    response.setContentType("application/json");
                    response.getWriter().write(jsonResponse.toString());
                }
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("error", "아이디와 이메일이 일치하지 않습니다.");
                response.setContentType("application/json");
                response.getWriter().write(jsonResponse.toString());
            }
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "허용되지 않는 요청 방식입니다.");
        }

        return null;
    }

    private String generateVerificationCode() {
        SecureRandom secureRand = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(secureRand.nextInt(10));
        }
        return code.toString();
    }

    private boolean sendEmail(String recipientEmail, String verificationCode) {
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
            message.setSubject("BitBank 비밀번호 재설정 요청");
            message.setText("당신의 인증번호는 " + verificationCode + "입니다.\n\n" +
                    "해당 인증번호는 3분간 유효합니다.");

            Transport.send(message);
            System.out.println("이메일이 성공적으로 전송되었습니다.");
            return true;
        } catch (MessagingException e) {
            System.err.println("이메일 전송에 문제가 생겼습니다. : " + e);
            return false;
        }
    }
}