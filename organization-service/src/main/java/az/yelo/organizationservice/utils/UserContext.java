package az.yelo.organizationservice.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserContext {
  public static final String CORRELATION_ID = "tmx-correlation-id";
  public static final String AUTH_TOKEN = "tmx-auth-token";
  public static final String USER_ID = "tmx-user-id";
  public static final String ORGANIZATION_ID = "tmx-organization-id";
  private String authToken = "";
  private String userId = "";
  private String organizationId = "";
  private String correlationId = "";

}
