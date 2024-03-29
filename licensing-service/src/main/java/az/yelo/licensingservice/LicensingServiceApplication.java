package az.yelo.licensingservice;

import az.yelo.licensingservice.utils.UserContextInterceptor;
import java.util.List;
import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
@RefreshScope
// This annotation provides /refresh for the app to reread the properties from config server
// Note a couple of things about the @RefreshScope annotation. This annotation only reloads the custom Spring properties you have in your application configuration.
//Items like your database configuration used by Spring Data won’t be reloaded by this annotation.


// Resilience4j's retry order
// Retry -> CircutBreaker -> RateLimiter -> TimeLimiter -> Bulkhead -> Function


@EnableDiscoveryClient // Activate Eureka client
@EnableFeignClients
public class LicensingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LicensingServiceApplication.class, args);
  }

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.US);

    return localeResolver;
  }

  @Bean
  public ResourceBundleMessageSource messageSourceResourceBundle() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setUseCodeAsDefaultMessage(
        true); // Does not throw an error if a message is not found, instead it returns the message code

    messageSource.setBasename("messages"); // Sets the base name of the languages properties file

    return messageSource;
  }
  @LoadBalanced // Gets all the instances of organization services
  @Bean
  public RestTemplate getRestTemplate() {
    RestTemplate template = new RestTemplate();
    List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
    interceptors.add(new UserContextInterceptor());
    template.setInterceptors(interceptors);

    return template;
  }
}

// Continue from 137