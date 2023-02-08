package az.yelo.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

// TODO: From now on we will start to implement pre and post filters for requests

public class ApiGatewayServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayServerApplication.class, args);
  }

}
