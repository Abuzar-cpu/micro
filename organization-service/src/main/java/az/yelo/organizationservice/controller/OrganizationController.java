package az.yelo.organizationservice.controller;

import az.yelo.organizationservice.model.Organization;
import az.yelo.organizationservice.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/organization")
public class OrganizationController {

  private final OrganizationService organizationService;

  @Autowired
  public OrganizationController(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @GetMapping("/{organizationId}")
  public Organization getById(@PathVariable("organizationId") String organizationId) {
    return this.organizationService.getOrganizationById(organizationId);
  }

  @PostMapping("/add")
  public Organization addOrganization(@RequestBody Organization organization) {
    return this.organizationService.addOrganization(organization);
  }
}
