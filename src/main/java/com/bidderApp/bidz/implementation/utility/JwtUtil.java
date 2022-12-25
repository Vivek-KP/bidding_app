
package com.bidderApp.bidz.implementation.utility;

import com.bidderApp.bidz.entity.PersonEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.method.P;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtUtil {
    private static final String SECRET_KEY = "secretkey";

    public static final Integer TOKEN_VALIDITY = 86400000;

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public String extractId(String token){
      Claims userData =  extractAllClaims(token);
      return (String) userData.get("id");
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        T claimR = claimsResolver.apply(claims);
        return claimR;
    }
    private Claims extractAllClaims(String token){
        Claims claim = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claim;
    }

    private Boolean isTokenExpired(String token){
        Boolean isExpired = extractExpiration(token).before(new Date());
        return isExpired;
    }
    public String generateToken(UserDetails userDetails,String id){
        Map<String,Object> claims = new HashMap<>();

        return createToken(claims,userDetails, id);
    }

    private String createToken(Map<String,Object> claims, UserDetails userDetails ,String id ){
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(""));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("id",id)
                .claim("roles",authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 86400000))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        Boolean userCheck = username.equals(userDetails.getUsername());
        Boolean tokenCheck = !isTokenExpired(token);
        return(userCheck && tokenCheck);
    }
}
