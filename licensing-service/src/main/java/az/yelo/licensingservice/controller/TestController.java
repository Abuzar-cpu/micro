package az.yelo.licensingservice.controller;

import az.yelo.licensingservice.model.Organization;
import az.yelo.licensingservice.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{licenseId}")
public class TestController {
  private final LicenseService licenseService;

  @Autowired
  public TestController(LicenseService licenseService) {
    this.licenseService = licenseService;
  }


  @GetMapping()
  public Organization getOrganizationByLicenseId(@PathVariable("licenseId") String licenseId) {
    try {
//      Thread.sleep(10000);

      return this.licenseService.getOrganizationByLicenseId(licenseId);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
