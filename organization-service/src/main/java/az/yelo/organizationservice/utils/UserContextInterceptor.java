package az.yelo.organizationservice.utils;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserContextInterceptor implements ClientHttpRequestInterceptor {
  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                      ClientHttpRequestExecution execution) throws IOException {
    HttpHeaders headers = request.getHeaders();
    headers.add(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());

    headers.add(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());

    log.debug("Intercepted request -> Correlation ID: " +
        request.getHeaders().get(UserContext.CORRELATION_ID));
    log.debug(
        "Intercepted request -> Auth Token: " + request.getHeaders().get(UserContext.AUTH_TOKEN));

    return execution.execute(request, body);
  }
}
