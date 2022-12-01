package com.dou.adm.security;

import io.jsonwebtoken.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;


/**
 * Created by Tu.Tran on 9/20/2018.
 */
@Service
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private static final String jwtSecret = new BigInteger(130, new SecureRandom()).toString(32);

    @Value("${jwt.expire.hours}")
    private int jwtExpireHrs;

    public String generateToken(JwtUser jwtUser) {

        Date expireDate = new DateTime().plusHours(jwtExpireHrs).toDate();

        return Jwts.builder()
                .setId(Long.toString(jwtUser.getId()))
                .setSubject(jwtUser.getUsername())
                .claim("auth",jwtUser.getAuthorities())
                .claim("isAdmin", jwtUser.getIsAdmin())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public JwtUser getJwtUser(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        long isAdmin = 0;
        try {
            isAdmin = Long.valueOf(String.valueOf(claims.get("isAdmin")));
        } catch (Exception e) { /*Do nothing*/ }

        JwtUser jwtUser = new JwtUser(claims.getSubject(), isAdmin);
        return jwtUser;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public String generatePassword(String originalPassword) {
        return BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
    }

    public boolean validatePassword(String originalPassword, String dbPassword){
        try{
            return BCrypt.checkpw(originalPassword, dbPassword);
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

    public String getSha256Hex(String text){
        String shaHex = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(text.getBytes("UTF-8"));
            byte[] digest = md.digest();

            shaHex = DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error(ex.getMessage());
        }
        return shaHex;
    }
}
