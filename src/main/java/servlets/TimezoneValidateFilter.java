package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static servlets.ParseUTC.parseUTC;

@WebFilter("/time/*")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ParseUTC parseUTC = new ParseUTC();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String notFormatedUTC = req.getParameter("timezone");

        String formattedUTC = null;

        try {
            if (notFormatedUTC != null) {
                formattedUTC = parseUTC(notFormatedUTC);

                Cookie timeCookie = new Cookie("lastTimeZone", formattedUTC);
                timeCookie.setPath("/");
                timeCookie.setMaxAge(3600);
                response.addCookie(timeCookie);
            }
            else {
                Cookie[] cookies = request.getCookies();
                if (cookies !=null){
                    for(Cookie cookie: cookies){
                        if("lastTimeZone".equals(cookie.getName())){
                            formattedUTC = cookie.getValue();
                            break;
                        }
                    }
                }
                if (formattedUTC == null){
                    formattedUTC = "+00:00";
                }
            }

            req.setAttribute("formattedUTC", formattedUTC);
            chain.doFilter(req, resp);

        }
        catch(IllegalArgumentException e){
            HttpServletResponse httpResponse = (HttpServletResponse) resp;
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UTC format: " + e.getMessage());
        }
    }

}