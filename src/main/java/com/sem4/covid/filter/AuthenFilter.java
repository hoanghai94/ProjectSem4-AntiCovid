package com.sem4.covid.filter;

import com.sem4.covid.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class AuthenFilter implements Filter {
    private final UserRepository repository;

    private static final String LOGIN_APP_URI = "/api/loginapp";

    private static final String LOGIN_WEB_URI = "/api/loginweb";

    private static final String PROTOCOL_API = "http://";

    private static final String LOGIN_FAIL_URI = "/api/fail";

    private static final String REGISTER_WEB_URI = "/api/registerweb";

    private static final String REGISTER_APP_URI = "/api/user";

    private static final String GET_USER_URI = "/api/users";

    public AuthenFilter(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String serverName = httpRequest.getServerName();
        int serverPort = httpRequest.getServerPort();

        String LOGIN_FAIL_REQUEST = PROTOCOL_API + serverName + ":" + serverPort + LOGIN_FAIL_URI;

        String URI = httpRequest.getRequestURI();
        if (URI.contains(REGISTER_WEB_URI) || URI.contains(REGISTER_APP_URI) || URI.contains(LOGIN_APP_URI) || URI.contains(LOGIN_WEB_URI) || URI.contains(GET_USER_URI)){
            filterChain.doFilter(servletRequest, servletResponse);
        }
        if (URI.contains(LOGIN_FAIL_URI)){
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else{
            String header = httpRequest.getHeader("accessToken");

            if (header == null || repository.checkToken(header) == null){
                httpResponse.sendRedirect(LOGIN_FAIL_REQUEST);

            }else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {
    }
}
