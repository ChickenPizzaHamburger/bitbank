package net.util;

import javax.servlet.http.*;

// Action 인터페이스: 로그인과 같은 기능을 수행하는 액션 클래스를 정의하기 위한 인터페이스
public interface Action {
    // execute 메서드: HTTP 요청과 응답을 처리하고, ActionForward 객체를 반환하는 메서드
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}