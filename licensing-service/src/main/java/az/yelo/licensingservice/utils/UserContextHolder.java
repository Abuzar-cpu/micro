package az.yelo.licensingservice.utils;

import org.springframework.util.Assert;

public class UserContextHolder {

  private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

  private UserContextHolder() {
  }

  public static UserContext getContext() {
    UserContext context = userContext.get();

    if (context == null) {
      context = createEmptyContext();
      userContext.set(context);
    }

    return userContext.get();
  }

  public static void setContext(UserContext context) {
    Assert.notNull(context, "Only non-null UserContext instances are permitted");
    userContext.set(context);
  }

  private static UserContext createEmptyContext() {
    return new UserContext();
  }
}
