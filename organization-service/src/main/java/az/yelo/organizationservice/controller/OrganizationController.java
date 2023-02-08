package az.yelo.organizationservice.controller;

import az.yelo.organizationservice.model.Organization;
import az.yelo.organizationservice.model.request.AddOrganizationRequest;
import az.yelo.organizationservice.model.response.GetOrganizationResponse;
import az.yelo.organizationservice.service.OrganizationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @GetMapping()
  public List<Organization> getAll() {
    return this.organizationService.getAllOrganizations();
  }
  @GetMapping("/{organizationId}")
  public ResponseEntity<GetOrganizationResponse> getById(
      @PathVariable("organizationId") String organizationId) {
    return this.organizationService.getOrganizationById(organizationId);
  }

  @PostMapping("/add")
  public ResponseEntity<Organization> addOrganization(
      @RequestBody AddOrganizationRequest organization) {
    return this.organizationService.addOrganization(organization);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Organization> deleteOrganization(String organizationId) {
    return this.organizationService.deleteOrganization(organizationId);
  }
}
