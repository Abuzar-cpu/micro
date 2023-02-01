package az.yelo.licensingservice.utils;

import java.io.IOException;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
//@Slf4j
public class UserContextFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

//  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws
      IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

    UserContextHolder.getContext().setCorrelationId(UserContext.CORRELATION_ID);
    UserContextHolder.getContext().setOrgnanizationId(UserContext.ORGANIZATION_ID);
    UserContextHolder.getContext().setUserId(UserContext.USER_ID);
    UserContextHolder.getContext().setAuthToken(UserContext.AUTH_TOKEN);

    chain.doFilter(httpServletRequest, servletResponse);
  }

  @Override
  public boolean isLoggable(LogRecord logRecord) {
    return true;
  }
}
