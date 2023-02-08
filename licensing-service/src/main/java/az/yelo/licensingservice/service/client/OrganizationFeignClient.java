package az.yelo.licensingservice.service.client;

import az.yelo.licensingservice.model.Organization;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(
    name = "organization-service",
    fallbackFactory = OrganizationLicenseFallbackFactory.class
)
public interface OrganizationFeignClient {
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/v1/organization/{organizationId}",
      consumes = "application/json"
  )
  Organization getOrganization(@PathVariable String organizationId);
}

@Slf4j
@Component
class OrganizationLicenseFallbackFactory implements FallbackFactory<OrganizationFeignClient> {

  @Override
  public OrganizationFeignClient create(Throwable cause) {
    String httpStatus =
        cause instanceof FeignException ? Integer.toString(((FeignException) cause).status()) : "";

    log.error("Could not connect to organization service: " + httpStatus);
    return null;
  }
}
