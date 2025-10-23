package com.artog.flashlearn.util;

public class JwtTokenUtil {
	
	
    // Issue new JWT with new tier
     public static String generateToken(String email, String userTier) {
    	 return "";
     }
	
	
	public static String getEmail(String token) {
	    return "";  /* Jwts.parser()
			            .setSigningKey(SECRET_KEY)
			            .parseClaimsJws(token)
			            .getBody()
			            .getSubject();*/
	}

}
