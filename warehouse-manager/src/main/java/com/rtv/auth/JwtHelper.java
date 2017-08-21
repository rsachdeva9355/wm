package com.rtv.auth;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.rtv.api.auth.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtHelper {

    private static final String ISSUER = "wm_auth";
    public static final int TOKEN_EXPIRY_HOURS = 30 * 24;  // 30 days
    private static final String SIGNING_KEY = "hellopeople";

    public static String issueToken(User user) {
        Date expiry = DateUtils.addHours(new Date(), TOKEN_EXPIRY_HOURS);
        return Jwts.builder()
            .setIssuer(ISSUER)
            .setIssuedAt(new Date())
            .setExpiration(expiry)
            .setSubject(user.getUsername())
            .signWith(SignatureAlgorithm.HS256, SIGNING_KEY.getBytes())
            .compact();
    }

    public static Jws<Claims> verifyAndDecode(String jwtToken) {
        return Jwts.parser() //
            .setSigningKey(SIGNING_KEY.getBytes()) //
            .parseClaimsJws(jwtToken);
    }

    public static class JwtToken {
        private String iss;
        private String sub;
        private Long exp;
        private Long iat;

        public JwtToken() {
        }

        public String getIss() {
            return iss;
        }

        public void setIss(String iss) {
            this.iss = iss;
        }

        public String getSub() {
            return sub;
        }

        public void setSub(String sub) {
            this.sub = sub;
        }

        public Long getExp() {
            return exp;
        }

        public void setExp(Long exp) {
            this.exp = exp;
        }

        public Long getIat() {
            return iat;
        }

        public void setIat(Long iat) {
            this.iat = iat;
        }
    }
}
