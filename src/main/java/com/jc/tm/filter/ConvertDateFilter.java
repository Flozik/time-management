package com.jc.tm.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ConvertDateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("{}",servletRequest.getAttribute("name"));
        String data = servletRequest.getParameter("name");

        String sDueDate = servletRequest.getParameter("dueDate");
        if(StringUtils.hasText(sDueDate)) {
            LocalDateTime dueDate = LocalDateTime.parse(sDueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            log.info("due date is: {}", dueDate);
        }

        log.info(data);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
