package com.ll.security_2025_01_06.global.rq;

import com.ll.security_2025_01_06.domain.member.member.entity.Member;
import com.ll.security_2025_01_06.domain.member.member.service.MemberService;
import com.ll.security_2025_01_06.global.exceptions.ServiceException;
import com.ll.security_2025_01_06.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Optional;

// Request/Response 를 추상화한 객체
// Request, Response, Cookie, Session 등을 다룬다.
@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {
    private final MemberService memberService;
    private final HttpServletRequest request;

    public void setLogin(String username) {
        UserDetails user = new User(
                username,
                "",
                List.of()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Member getActor() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) return null;

        Authentication authentication = context.getAuthentication();
        if (authentication == null) return null;
        if (authentication.getPrincipal() == null || authentication.getPrincipal() instanceof  String) return null;

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String username = user.getUsername();

        return memberService.findByUsername(username).get();
    }
}
