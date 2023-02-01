package az.yelo.licensingservice.service;

import static java.lang.Thread.sleep;

import az.yelo.licensingservice.config.ServiceConfig;
import az.yelo.licensingservice.model.License;
import az.yelo.licensingservice.model.Organization;
import az.yelo.licensingservice.model.request.CreateLicenseRequest;
import az.yelo.licensingservice.model.request.UpdateLicenseRequest;
import az.yelo.licensingservice.model.response.CreateLicenseResponse;
import az.yelo.licensingservice.repository.LicenseRepository;
import az.yelo.licensingservice.service.client.OrganizationFeignClient;
import az.yelo.licensingservice.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final OrganizationFeignClient client;
    MessageSource messageSource;

    private ServiceConfig serviceConfig;

    @Autowired
    public LicenseService(MessageSource messageSource, LicenseRepository licenseRepository,
                          OrganizationFeignClient client) {
        this.messageSource = messageSource;
        this.licenseRepository = licenseRepository;
        this.client = client;
    }

    public List<License> getAllLicenses() {
        List<License> licenses = this.licenseRepository.findAll();

        if (licenses.isEmpty())
            log.info("No licenses found");

        return licenses;
    }


    ////////////////////////////////////////TEST AREA///////////////////////////////////////////////////////

    private void randomlyRunLong() throws TimeoutException{
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        if (randomNum==3) sleep();
    }

    private void sleep() throws TimeoutException{
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
    public List<License> getLicenseByOrganization(String organizationId) throws TimeoutException{
        List<License> licenses = this.licenseRepository.findByOrganizationId(organizationId);

        log.info("getLicenseByOrganizationID correlation id: " + UserContextHolder.getContext().getCorrelationId());

        if (licenses.isEmpty()) {
            log.info("License does not exist for organization: " + organizationId);

            return licenses;
        }

        randomlyRunLong();
        return licenses;
    }

    private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
        log.info(t.getMessage());
        return List.of(new License().withId("000000-00-000000").withOrganizationId(organizationId).withProductName("Sorry, no licensing informatino currently available"));
    }

    ////////////////////////////////////////END OF TEST AREA///////////////////////////////////////////////////////

    public Organization getOrganizationByLicenseId(String licenseId) {
        License license = this.licenseRepository.findByLicenseId(licenseId);
        if (license == null) {
            return null;
        }

        Organization org = client.getOrganization(license.getOrganizationId());
        org.setId(license.getOrganizationId());
        return org;
    }

    public License getLicense(String licenseId, String organizationId) {
        License license =
            this.licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            return null;
        }

        log.info(this.messageSource.getMessage("license.get.message", null, null));

        return license.withComment(this.serviceConfig.getProperty());
    }

    public ResponseEntity<CreateLicenseResponse> createLicense(CreateLicenseRequest license) {
        License newLicense = new License();
        newLicense.setDescription(license.getDescription());
        newLicense.setOrganizationId(license.getOrganizationId());
        newLicense.setProductName(license.getProductName());
        newLicense.setLicenseType(license.getLicenseType());
        newLicense.setComment(license.getComment());

        this.licenseRepository.save(newLicense);

        return ResponseEntity.ok(license.mapToResponse());
    }

    public ResponseEntity<License> updateLicense(UpdateLicenseRequest license) {
        License license1 = this.licenseRepository.findByLicenseId(license.getLicenseId());
        if (license1 == null) {
            return ResponseEntity.notFound().build();
        }
        license1.setOrganizationId(license.getOrganizationId());
        this.licenseRepository.save(license1);

        return ResponseEntity.ok(license1.withComment(this.serviceConfig.getProperty()));
    }

    public ResponseEntity<License> deleteLicense(String licenseId) {
        License license = this.licenseRepository.findByLicenseId(licenseId);
        if (license == null) {
            log.warn("License with id " + licenseId + " does not exist");
            return ResponseEntity.notFound().build();
        }

        this.licenseRepository.delete(license);
        log.info("License deleted: " + licenseId);
        return ResponseEntity.ok(license);
    }

}
