package com.example.rest.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 从request中获取token，并把token转换成用户，放置到当前的spring context内
 * 这个类必须写一个@Service注解，否则Spring不会把它加载作为filter
 */
@Service
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private final static Log log = LogFactory.getLog(AuthenticationTokenFilter.class);
    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authToken = null;

        // 下面的代码从Http Header的Authorization中获取token，也可以从其他header, cookie等中获取，看客户端怎么传递token
        String requestHeader = httpServletRequest.getHeader("Authorization");
        if (requestHeader != null && requestHeader.startsWith("bearer")) {
            authToken = requestHeader.substring(7);
        }
        if (log.isTraceEnabled()) {
            log.trace("token is " + authToken);
        }
        if (authToken != null) {
            UserDetails user = null;
            user = tokenService.getUserFromToken(authToken);
            if (user != null) {
                // 把user设置到SecurityContextHolder内，以便Spring使用
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
