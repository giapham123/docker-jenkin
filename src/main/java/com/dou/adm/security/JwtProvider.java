package com.dou.adm.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.dou.adm.security.JwtUtils.PROFILES;


/**
 * Created by Tu.Tran on 9/20/2018.
 */
@Service
public class JwtProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);
    private static final String jwtSecret = new BigInteger(130, new SecureRandom()).toString(32);

    @Value("${jwt.expire.hours}")
    private int jwtExpireHrs;

    @Autowired
    private ObjectMapper objectMapper;

    public String generateToken(JwtUser jwtUser) {
        Date expireDate = new DateTime().plusHours(jwtExpireHrs).toDate();

        String profileJsonData = null;
        try {
            profileJsonData = this.objectMapper.writeValueAsString(jwtUser.getProfiles());
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format("Can not write UserProject of %s to JSON string", jwtUser.getUsername()), e);
        }

        return Jwts.builder()
                // HEADERS
                .setId(Long.toString(jwtUser.getId()))
                .setSubject(jwtUser.getUsername())

                // PAYLOADS
                .claim(PROFILES, profileJsonData)

                // SIGNATURE
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)

                // BUILD TOKEN
                .compact();
    }

    public JwtUser getJwtUser(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return JwtUtils.retrieveUserFromClaims(claims, objectMapper);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            LOGGER.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOGGER.warn("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty.");
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
            LOGGER.error(e.getMessage());
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
            LOGGER.error(ex.getMessage());
        }
        return shaHex;
    }
}
