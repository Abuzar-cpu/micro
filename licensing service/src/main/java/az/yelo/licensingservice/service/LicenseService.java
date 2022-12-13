package az.yelo.licensingservice.service;

import az.yelo.licensingservice.config.ServiceConfig;
import az.yelo.licensingservice.model.License;
import az.yelo.licensingservice.model.request.CreateLicenseRequest;
import az.yelo.licensingservice.model.request.UpdateLicenseRequest;
import az.yelo.licensingservice.model.response.CreateLicenseResponse;
import az.yelo.licensingservice.repository.LicenseRepository;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class
LicenseService {

  MessageSource messageSource;

  @Autowired
  ServiceConfig serviceConfig;
  private final LicenseRepository licenseRepository;

  @Autowired
  public LicenseService(MessageSource messageSource, LicenseRepository licenseRepository) {
    this.messageSource = messageSource;
    this.licenseRepository = licenseRepository;
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
}
