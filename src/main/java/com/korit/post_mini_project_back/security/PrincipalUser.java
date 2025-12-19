package com.korit.post_mini_project_back.security;

import com.korit.post_mini_project_back.entity.User;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;


@Getter
public class PrincipalUser extends DefaultOAuth2User {
    private final User user;


    // JWT 인증 및 OAuth2 공통으로 사용할 기본 권한 설정
    private static final List<SimpleGrantedAuthority> DEFAULT_AUTHORITIES =
            List.of(new SimpleGrantedAuthority("ROLE_USER")); // ⭐ 모든 사용자에게 ROLE_USER 부여


    // 기존 OAuth2 인증용 생성자 (수정: 권한을 DEFAULT_AUTHORITIES로 변경)
    public PrincipalUser(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, User user) {
        // 기존 authorities 대신 DEFAULT_AUTHORITIES를 사용하거나, 전달받은 authorities가 ROLE_USER만 담고 있는지 확인
        super(authorities, attributes, nameAttributeKey);
        this.user = user;
    }


    public static PrincipalUser getAuthenticationPrincipalUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        return principalUser;
    }



    // ⭐ JWT 인증 필터 오류 해결용 생성자 (수정: user.getRole() 관련 코드 제거)
//    public PrincipalUser(User user) {
//        super(
//                DEFAULT_AUTHORITIES, // 👈 고정된 권한 사용
//                Map.of("id", user.getUserId()),
//                "id"
//        );
//        this.user = user;
//    }
}

//
//package com.korit.post_mini_project_back.security;
//
//import com.korit.post_mini_project_back.entity.User;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//@Getter
//public class PrincipalUser extends DefaultOAuth2User {
//    private User user;
//
//    public PrincipalUser(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, User user) {
//        super(authorities, attributes, nameAttributeKey);
//        this.user = user;
//    }
//
//}