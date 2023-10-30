package security.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {


    public static final  String SECRET = "f7Y1vS9/ISBuGiNIzpEr4aay8eLtJfySl141GccRlOdoSIVNwlOFupaxvnShID7xNw+Mo9dQB/YN1gzoQln6J1mnbAWytgXRFlVXp1DUBXhia48poJSHm3JmSmhj7JCKD5kRX7fvzpcNaJATH3MwxPJD1Q5Msdn2Yd1wp/kyGna1Tojh8fphIBBR1tNwnpU0acNlMEtXOqDXUm2Xd3CJx7xq4nKWi4J35Pqxqh0zfDyBWxT4aLUb6VNu2qhMtIMWrEW1KXH8VpstI4H8iUFUmiT2Zsyq11ecnDqywA51pUUDIGogAy41Mmjo9Q7Jdg4FC6Wy+wYNgtoc/CGrGEm0AdunRX/Ki/XlmkpqiMY4+Ms=\n";


    public String extractUsername(String token){

        return  extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiation(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver ) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token){
        return extractExpiation(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    public String generateToken(String name){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims,name);
    }


    private String createToken(Map<String, Object> claims, String name) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(name)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
