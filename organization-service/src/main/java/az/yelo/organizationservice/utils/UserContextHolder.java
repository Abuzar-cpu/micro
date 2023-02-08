package az.yelo.organizationservice.utils;

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

  public static UserContext createEmptyContext() {
    return new UserContext();
  }
}
