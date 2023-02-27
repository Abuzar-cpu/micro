package az.yelo.licensingservice.service.client;

import az.yelo.licensingservice.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {
  private final RestTemplate restTemplate;

  @Autowired
  public OrganizationRestTemplateClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Organization getOrganization(String organizationId) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.set("Host", "localhost:8072");

//    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<Organization> restExchange = restTemplate.exchange(
        "/v1/organization/{organizationId}", // Sending request through gateway server for better security, logging and more
        HttpMethod.GET,
        null,
        Organization.class,
        organizationId
    );

    return restExchange.getBody();
  }
}
