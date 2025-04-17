package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;


@WebServlet("/time")
public class TimeZoneServlet extends HttpServlet {
    private TemplateEngine engine;
    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        String templatePath = getServletContext().getRealPath("/WEB-INF/templates/");
        resolver.setPrefix(templatePath + "/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        engine.addTemplateResolver(resolver);

        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");

        String formattedUTC = (String) req.getAttribute("formattedUTC");
        ZonedDateTime timeWthOffset = ZonedDateTime.now(ZoneOffset.of(formattedUTC));
        String currentTime = timeWthOffset.format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss "));

        Map<String, Object> model = new LinkedHashMap<>();
        model.put("currentTime",currentTime);
        model.put("currentUTC",formattedUTC);

        Context simpleContext = new Context(
                req.getLocale(),
                model
        );

        engine.process("time",simpleContext,resp.getWriter());
        resp.getWriter().close();
    }
}