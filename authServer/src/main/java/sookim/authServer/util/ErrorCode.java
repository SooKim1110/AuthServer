package sookim.authServer.util;

import lombok.Getter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter
public enum ErrorCode {

//     Common
    INTERNAL_SERVER_ERROR(500, "C001", "서버 에러가 발생했습니다."),
//    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
//    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),

//    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    // Member
//    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
//    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),

    //Authentication
    LOGIN_CREDENTIAL_INVALID(401, "A001", "아이디 혹은 패스워드가 틀렸습니다."),
    TOKEN_NOT_FOUND(401, "A002", "토큰이 존재하지 않습니다."),
    EXPIRED_TOKEN(401,"A003","토큰 유효기간이 만료되었습니다."),
    INVALID_TOKEN(401,"A004","유효하지 않은 토큰입니다."),

    LOGOUT_INVALID(500, "A005", "서버에 로그아웃 오류가 있습니다.");

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}