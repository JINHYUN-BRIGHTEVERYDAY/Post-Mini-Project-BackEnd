package com.korit.post_mini_project_back.filter;

import com.korit.post_mini_project_back.entity.User;
import com.korit.post_mini_project_back.jwt.JwtTokenProvider;
import com.korit.post_mini_project_back.security.PrincipalUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
//    private final UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        // 잘못된 토큰이 들어오는 상황
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            // 다음 필터로 넘겨버리기
            filterChain.doFilter(request, response);
            return;
        }

        // 정상적인 토큰인 경우
        String accessToken = bearerToken.replaceAll("Bearer ", "");
        // 유효하지 않은 토큰의 경우 다음 필터로 넘겨버리기
        if (!jwtTokenProvider.validateToken(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 유효한 경우
//        int userId = jwtTokenProvider.getuserId(accessToken);
//        User foundUser = userMapper.findUserByUserId(userId);
        int userId = jwtTokenProvider.getuserId(accessToken);
        User foundUser = null;
        if (foundUser == null) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println(foundUser);

        // 권한 부여하기
        PrincipalUser principalUser = new PrincipalUser(foundUser);
        String password = "";
        Collection<? extends GrantedAuthority> authorities = principalUser.getAuthorities();

        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(principalUser, password, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 다음 필터 -> 인증 처리
        filterChain.doFilter(request, response);
    }

}