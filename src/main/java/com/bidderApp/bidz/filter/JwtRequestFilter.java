package com.bidderApp.bidz.filter;

import com.bidderApp.bidz.entity.JwtToken;
import com.bidderApp.bidz.entity.UserDisable;
import com.bidderApp.bidz.repository.BannedUserRepository;
import com.bidderApp.bidz.repository.JWTRepository;
import com.bidderApp.bidz.service.NonAuthService;
import com.bidderApp.bidz.implementation.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NonAuthService nonAuthService;

    @Autowired
    private JWTRepository jwtRepository;

    @Autowired
    private BannedUserRepository bannedUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;
        JwtToken jwtToken = null;
        UserDisable disabledUser = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            jwtToken = jwtRepository.findByJwtToken(jwt);
            email = jwtUtil.extractUsername(jwt);
            String id =jwtUtil.extractId(jwt);
            disabledUser = bannedUserRepository.findByBannedUserId(id);


        }
        if(email!=null && SecurityContextHolder.getContext().getAuthentication() == null && Objects.isNull(jwtToken) && Objects.isNull(disabledUser) && Objects.isNull(disabledUser)) {
            UserDetails userDetails = null;
            userDetails = this.nonAuthService.loadUserByUsername(email);
            if(jwtUtil.validateToken(jwt,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
