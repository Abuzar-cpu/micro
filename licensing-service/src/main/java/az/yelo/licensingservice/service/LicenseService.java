package az.yelo.licensingservice.service;

import az.yelo.licensingservice.config.ServiceConfig;
import az.yelo.licensingservice.model.License;
import az.yelo.licensingservice.model.Organization;
import az.yelo.licensingservice.model.request.CreateLicenseRequest;
import az.yelo.licensingservice.model.request.UpdateLicenseRequest;
import az.yelo.licensingservice.model.response.CreateLicenseResponse;
import az.yelo.licensingservice.model.response.DeleteLicenseResponse;
import az.yelo.licensingservice.model.response.GetLicenseByOrganizationIdResponse;
import az.yelo.licensingservice.model.response.GetLicenseResponse;
import az.yelo.licensingservice.model.response.UpdateLicenseResponse;
import az.yelo.licensingservice.repository.LicenseRepository;
import az.yelo.licensingservice.service.client.OrganizationFeignClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LicenseService {

  private final LicenseRepository licenseRepository;
  private final OrganizationFeignClient client;
  private final ModelMapper modelMapper;
  MessageSource messageSource;

  private ServiceConfig serviceConfig;

  public LicenseService(MessageSource messageSource, LicenseRepository licenseRepository,
                        OrganizationFeignClient client, ModelMapper modelMapper) {
    this.messageSource = messageSource;
    this.licenseRepository = licenseRepository;
    this.client = client;
    this.modelMapper = modelMapper;
  }

  public List<License> getAllLicenses() {
    List<License> licenses = this.licenseRepository.findAll();

    if (licenses.isEmpty()) {
      log.info("No licenses found");
    }

    return licenses;
  }


  ////////////////////////////////////////TEST AREA///////////////////////////////////////////////////////

  private void randomlyRunLong() throws TimeoutException {
    Random rand = new Random();
    int randomNum = rand.nextInt(3) + 1;
    if (randomNum == 3) {
      sleep();
    }
  }

  private void sleep() throws TimeoutException {
    try {
      Thread.sleep(5000);
      throw new java.util.concurrent.TimeoutException();
    } catch (InterruptedException e) {
      log.debug(e.getMessage());
    }
  }

  @CircuitBreaker(
      name = "licenseService",
      fallbackMethod = "buildFallbackLicenseList"
  )
  @Bulkhead(
      name = "bulkheadLicenseService",
      fallbackMethod = "buildFallbackLicenseList"
  )
  @Retry(
      name = "retryLicenseService",
      fallbackMethod = "buildFallbackLicenseList"
  )
  @RateLimiter(
      name = "licenseService",
      fallbackMethod = "buildFallbackLicenseList"
  )
  public ResponseEntity<List<GetLicenseByOrganizationIdResponse>> getLicenseByOrganization(String organizationId)
      throws TimeoutException {
    List<License> licenses = this.licenseRepository.findByOrganizationId(organizationId);

    if (licenses.isEmpty()) {
      log.info("License does not exist for organization: " + organizationId);

      return ResponseEntity.notFound().build();
    }

    randomlyRunLong();
    List<GetLicenseByOrganizationIdResponse> result = licenses.stream().map(license -> this.modelMapper.map(license, GetLicenseByOrganizationIdResponse.class)).toList();
    return ResponseEntity.ok(result);
  }

  private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
    log.info(t.getMessage());
    return List.of(new License().builder().licenseId("000000-00-000000").organizationId(organizationId)
        .productName("Sorry, no licensing information currently available").build());
  }

  ////////////////////////////////////////END OF TEST AREA///////////////////////////////////////////////////////

  // Feign client usage area
  public Organization getOrganizationByLicenseId(String licenseId) {
    License license = this.licenseRepository.findByLicenseId(licenseId);
    if (license == null) {
      return null;
    }

    return client.getOrganization(license.getOrganizationId());
  }

  public ResponseEntity<GetLicenseResponse> getLicense(String licenseId, String organizationId) {
    License license =
        this.licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

    if (license == null) {
      return ResponseEntity.notFound().build();
    }

    license.add(Link.of("/v1/organization/" + organizationId + "/license/" + licenseId).withSelfRel());

    var result = modelMapper.map(license, GetLicenseResponse.class);
    return ResponseEntity.ok(result);
  }

  public ResponseEntity<CreateLicenseResponse> createLicense(CreateLicenseRequest license) {
    if (this.client.getOrganization(license.getOrganizationId()) != null) {

      var save = this.modelMapper.map(license, License.class);

      this.licenseRepository.save(save);
      return ResponseEntity.ok(modelMapper.map(license, CreateLicenseResponse.class));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  public ResponseEntity<UpdateLicenseResponse> updateLicense(UpdateLicenseRequest license) {
    License updated = this.modelMapper.map(license, License.class);
    this.licenseRepository.save(updated);
    return ResponseEntity.ok(this.modelMapper.map(license, UpdateLicenseResponse.class));
  }

  public ResponseEntity<DeleteLicenseResponse> deleteLicense(String licenseId) {
    License license = this.licenseRepository.findByLicenseId(licenseId);
    if (license == null) {
      log.warn("License with id " + licenseId + " does not exist");
      return ResponseEntity.notFound().build();
    }

    this.licenseRepository.delete(license);
    log.info("License deleted: " + licenseId);
    return ResponseEntity.ok(this.modelMapper.map(license, DeleteLicenseResponse.class));
  }

}
