package az.yelo.organizationservice.utils;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserContextFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;

    UserContextHolder.getContext().setCorrelationId(request.getHeader(UserContext.CORRELATION_ID));
    UserContextHolder.getContext().setUserId(request.getHeader(UserContext.USER_ID));
    UserContextHolder.getContext().setAuthToken(request.getHeader(UserContext.AUTH_TOKEN));
    UserContextHolder.getContext()
        .setOrganizationId(request.getHeader(UserContext.ORGANIZATION_ID));

    log.debug("UserContext Correlation ID: " + UserContextHolder.getContext().getCorrelationId());

    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
