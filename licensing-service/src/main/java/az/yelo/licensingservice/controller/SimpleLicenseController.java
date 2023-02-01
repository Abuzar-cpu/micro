package az.yelo.licensingservice.controller;

import az.yelo.licensingservice.model.License;
import az.yelo.licensingservice.service.LicenseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/licenses/")
public class SimpleLicenseController {

  private final LicenseService licenseService;

  @Autowired
  public SimpleLicenseController(LicenseService licenseService) {
    this.licenseService = licenseService;
  }

  @GetMapping
  public ResponseEntity<List<License>> getAllLicenses() {
    List<License> licenses = this.licenseService.getAllLicenses();

    return ResponseEntity.ok(licenses);
  }

}
