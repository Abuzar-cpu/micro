package az.yelo.licensingservice.utils;

public class UserContextHolder {

  private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

  public static UserContext getContext() {
    UserContext context = userContext.get();

    if(context == null) {
      context = createEmptyContext();
      userContext.set(context);
    }

    return userContext.get();
  }

  public static void setContext(UserContext context) {
    userContext.set(context);
  }

  private static UserContext createEmptyContext() {
    return new UserContext();
  }
}
