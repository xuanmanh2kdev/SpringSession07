package fa.training.eventbox.security;

import fa.training.eventbox.model.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private static final String SECRET_KEY = "MiuCoffee1810";
    public String generateToken(Authentication authentication) {
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        LocalDateTime expiredTime = LocalDateTime.now().plusHours(1);

        return Jwts.builder()
                .setSubject(authentication.getName()) // username: email
                .claim("authorities", roles)
                .setExpiration(Date.from(expiredTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            List<GrantedAuthority> authorityList = Arrays.stream(claims.get("authorities").toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            User principal = new User(claims.getSubject(), "", authorityList);
            return new UsernamePasswordAuthenticationToken(principal, null, authorityList);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        List<GrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.name()));
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("cusomer01@gmail.com", "", roles);
        TokenProvider tokenProvider = new TokenProvider();
        String token = tokenProvider.generateToken(authentication);
        System.out.println(token);
        System.out.println(tokenProvider.getAuthentication(token));
        System.out.println(tokenProvider.getAuthentication(token + "_invalid_token"));

    }
}
