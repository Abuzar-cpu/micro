package az.yelo.licensingservice.service;

import az.yelo.licensingservice.config.ServiceConfig;
import az.yelo.licensingservice.model.License;
import az.yelo.licensingservice.model.Organization;
import az.yelo.licensingservice.model.request.CreateLicenseRequest;
import az.yelo.licensingservice.model.request.UpdateLicenseRequest;
import az.yelo.licensingservice.model.response.CreateLicenseResponse;
import az.yelo.licensingservice.repository.LicenseRepository;
import az.yelo.licensingservice.service.client.OrganizationFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LicenseService {

  MessageSource messageSource;

  @Autowired
  private ServiceConfig serviceConfig;
  private final LicenseRepository licenseRepository;
  private final OrganizationFeignClient client;

  @Autowired
  public LicenseService(MessageSource messageSource, LicenseRepository licenseRepository,
                        OrganizationFeignClient client) {
    this.messageSource = messageSource;
    this.licenseRepository = licenseRepository;
    this.client = client;
  }

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

    return license.withComment(serviceConfig.getProperty());
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
    this.licenseRepository.delete(license);
    return ResponseEntity.ok(license);
  }

//  public GetLicenseResponse getLicense(String organizationId, String licenseId, String clientType) {
//    License license =
//        this.licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
//    if (license == null) {
//      return null;
//    }
//
//    az.yelo.licensingservice.model.Organization organization =
//        retrieveOrganizationInfo(organizationId, clientType);
//    if (organization != null) {
//      license.setOrganizationId(organization.getName());
////      license.setContactName(organization.getContactName());
//    }
//
//    return new GetLicenseResponse(license.getDescription(), license.getOrganizationId(),
//        license.getProductName(), license.getLicenseType(), license.getComment());
//  }
}
