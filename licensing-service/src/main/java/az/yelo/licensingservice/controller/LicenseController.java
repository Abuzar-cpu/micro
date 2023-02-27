package az.yelo.licensingservice.controller;

import az.yelo.licensingservice.model.request.CreateLicenseRequest;
import az.yelo.licensingservice.model.request.UpdateLicenseRequest;
import az.yelo.licensingservice.model.response.CreateLicenseResponse;
import az.yelo.licensingservice.model.response.DeleteLicenseResponse;
import az.yelo.licensingservice.model.response.GetLicenseByOrganizationIdResponse;
import az.yelo.licensingservice.model.response.GetLicenseResponse;
import az.yelo.licensingservice.model.response.UpdateLicenseResponse;
import az.yelo.licensingservice.service.LicenseService;
import az.yelo.licensingservice.utils.UserContextHolder;
import java.util.List;
import java.util.concurrent.TimeoutException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Tells spring that is a REST based application. JSON conversion will be under the hood
@RequestMapping(value = "v1/organization/{organizationId}/license/") // Exposes the endpoint
@Slf4j
@AllArgsConstructor
public class LicenseController {
  private final LicenseService licenseService;


  @GetMapping
  public ResponseEntity<List<GetLicenseByOrganizationIdResponse>> getLicenseByOrganizationId(
      @PathVariable("organizationId") String organizationId) throws
      TimeoutException {
    log.debug("LicenseServiceController Correlation id: {}",
        UserContextHolder.getContext().getCorrelationId());

    return this.licenseService.getLicenseByOrganization(organizationId);
  }

  @GetMapping(value = "{licenseId}")
  public ResponseEntity<GetLicenseResponse> getLicense(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId) {
    return this.licenseService.getLicense(licenseId, organizationId);
  }

  @PutMapping
  public ResponseEntity<UpdateLicenseResponse> updateLicense(@RequestBody UpdateLicenseRequest license) {
    return this.licenseService.updateLicense(license);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED) // 201 -> created
  public ResponseEntity<CreateLicenseResponse> createLicense(
      @RequestBody CreateLicenseRequest license) {
    if (license == null) {
      return ResponseEntity.badRequest().build();
    }

    return this.licenseService.createLicense(license);
  }

  // Done
  @DeleteMapping(value = "{licenseId}")
  public ResponseEntity<DeleteLicenseResponse> deleteLicense(@PathVariable("licenseId") String licenseId) {
    return this.licenseService.deleteLicense(licenseId);
  }

}
