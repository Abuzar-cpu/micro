package az.yelo.licensingservice.controller;

import az.yelo.licensingservice.model.License;
import az.yelo.licensingservice.model.Organization;
import az.yelo.licensingservice.model.request.CreateLicenseRequest;
import az.yelo.licensingservice.model.request.UpdateLicenseRequest;
import az.yelo.licensingservice.model.response.CreateLicenseResponse;
import az.yelo.licensingservice.model.response.GetLicenseResponse;
import az.yelo.licensingservice.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Tells spring that is a REST based application. JSON conversion will be under the hood
@RequestMapping(value = "v1/organization/{organizationId}/license/") // Exposes the endpoint
public class LicenseController {

  private final LicenseService licenseService;

  @Autowired
  public LicenseController(LicenseService licenseService) {
    this.licenseService = licenseService;
  }

  // DONE
  @GetMapping(value = "{licenseId}")
  public ResponseEntity<GetLicenseResponse> getLicense(@PathVariable("organizationId") String organizationId,
                                                       @PathVariable("licenseId") String licenseId) {
    License license = this.licenseService.getLicense(licenseId, organizationId);

    if(license == null) {
      return ResponseEntity.notFound().build();
    }

    license.add(
        Link.of("/v1/organization/" + organizationId + "/license/" + licenseId).withSelfRel());

    return ResponseEntity.ok(new GetLicenseResponse(license.getDescription(), license.getOrganizationId(), license.getProductName(), license.getLicenseType(), license.getComment()));
  }

//  @GetMapping("/{licenseId}/{clientType}")
//  public GetLicenseResponse getLicenseWithClient(@PathVariable("organizationId") String organizationId,
//                                                 @PathVariable("licenseId") String licenseId,
//                                                 @PathVariable("clientType") String clientType) {
//    return this.licenseService.getLicense(organizationId, licenseId, clientType);
//  }

  // DONE
  @PutMapping
  public ResponseEntity<License> updateLicense(@RequestBody UpdateLicenseRequest license) {
    return this.licenseService.updateLicense(license);
  }

  // DONE
  @PostMapping
  public ResponseEntity<CreateLicenseResponse> createLicense(@RequestBody CreateLicenseRequest license) {
    if (license == null)
      return ResponseEntity.badRequest().build();

    return this.licenseService.createLicense(license);
  }

  @DeleteMapping(value = "{licenseId}")
  public ResponseEntity<License> deleteLicense(@PathVariable("licenseId") String licenseId) {
    return this.licenseService.deleteLicense(licenseId);
  }

}
