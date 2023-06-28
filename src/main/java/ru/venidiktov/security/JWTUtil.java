package ru.venidiktov.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    //Формирование JWT токена для клиента с данными клиента
    public String generateToken(String username) {
        //Срок жизни токена
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details") //Что хранится в JWT токене
                .withClaim("username", username) //Пара ключ значение данных пользователя (Может быть много)
                .withIssuedAt(new Date()) //Время когда был выдан JWT токен
                .withIssuer("Venidiktov G.A") //Кто выдал токен
                .withExpiresAt(expirationDate) //До какого времени токен действителен
                .sign(Algorithm.HMAC256(secret)); //Secret участвующий в формировании токена и гарантирующий его подлинность (есть только на сервере)
    }

    //Валидация JWT который передает пользователя и доставание из JWT данных пользователя
    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        //Верификация токена
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details") //У токена должен быть такой Subject
                .withIssuer("Venidiktov G.A") //Токен должен быть выдан этим субъектом
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
