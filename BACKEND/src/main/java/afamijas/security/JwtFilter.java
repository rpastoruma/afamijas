package afamijas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebFilter(urlPatterns = "/private/*")
public class JwtFilter implements Filter 
{
  public static final String SECRET = "Kjdfk823js9f238jlg_dsf9823";

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException 
  {
  }
 
  @Override
  public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException 
  {
    final HttpServletRequest request = (HttpServletRequest) req;
    final HttpServletResponse response = (HttpServletResponse) res;
    final String authHeader = request.getHeader("authorization");
    
    if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) 
    {
	  response.setHeader("Access-Control-Allow-Origin", "*");
	  response.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,PATCH,OPTIONS,HEAD");
	  response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
      response.setStatus(HttpServletResponse.SC_OK);
      chain.doFilter(req, res);
    } 
    else 
    {
 
      if (authHeader == null || !authHeader.startsWith("Bearer ")) 
      {
    	response.setHeader("Access-Control-Allow-Origin", "*");
   	    response.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,PATCH,OPTIONS,HEAD");
  	    response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return;
      }
 
      final String token = authHeader.substring(7);
 
      try 
      {
        final Claims claims = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(JwtFilter.SECRET)).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        String id = claims.getId();
        List<String> roles = Arrays.asList(claims.getAudience().split("\\s*,\\s*"));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new JwtUser(id, username, roles, "", ""), null,	null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      catch (final JwtException e) 
      {
    	response.setHeader("Access-Control-Allow-Origin", "*");
   	    response.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,PATCH,OPTIONS,HEAD");
  	    response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return;
      }
 
      chain.doFilter(req, res);
    }
  }
 
  @Override
  public void destroy() 
  {

  }
  
}