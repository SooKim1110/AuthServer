package sookim.authServer.util.jwt;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import sookim.authServer.util.ErrorCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof BadCredentialsException || authException instanceof InternalAuthenticationServiceException){
            setResponse(response, ErrorCode.LOGIN_INPUT_INVALID);
        }
        else {
            System.out.println("authException = " + authException);

        }

//        System.out.println("authException. = " + authException.getMessage());
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

    }
    public void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.sendError(errorCode.getStatus(), errorCode.getMessage());
    }
}
