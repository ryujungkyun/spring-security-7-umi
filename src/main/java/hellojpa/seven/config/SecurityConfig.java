package hellojpa.seven.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // role hierarchy
    @Bean
    public RoleHierarchy roleHierarchy() {

        return RoleHierarchyImpl.withRolePrefix("ROLE_")
                .role("ADMIN").implies("USER")
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/logout"));

        // 수많은 필터중 csrf 필터를 disable 시킴
        http
//                .csrf(csrf -> csrf.disable());
                .csrf(Customizer.withDefaults());

        // 로그인 필터 설정
        http
                .formLogin(login -> login
                        .loginProcessingUrl("/login")
                        .loginPage("/login"));


        // rembmer me 설정
        http
                .rememberMe(me -> me
                        //프로퍼티로 자동으로 넣어주게 설정하는것이 더 좋다
                        .key("vmfhaltmskdlsvmfhaltmskdlsvmfhaltmskdls")
                        .rememberMeParameter("remember-me")
                        //2주 동안
                        .tokenValiditySeconds(14 * 24 * 60 * 60)
                );

        // 세션 스테이트리스
/*
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
*/

        // 인가 필터 설정
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/join").permitAll()
                        .requestMatchers("/login").permitAll()
                        //자동으로 로그인 권한 페이지로 이동한다.
                        .requestMatchers("/user").hasAnyRole("USER")
                        .requestMatchers("/admin").access(customAuthorizationManager())
                        .anyRequest().denyAll()
                );

        //세션 고정 공격 방지 방법
        http
                .sessionManagement(session -> session
                        .sessionFixation().changeSessionId());

        return http.build();
    }

    private AuthorizationManager<RequestAuthorizationContext> customAuthorizationManager() {

        return (authentication, context) -> {

            boolean allowed =
                    authentication.get().getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            // 자격이 맞는지

            // 사용할 수 있는 카운트

            // 비즈니스 ,개인


            return new AuthorizationDecision(allowed);
        };
    }
}
