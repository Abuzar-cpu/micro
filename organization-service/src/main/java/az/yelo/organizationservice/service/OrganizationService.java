package az.yelo.organizationservice.service;

import az.yelo.organizationservice.model.Organization;
import az.yelo.organizationservice.model.request.AddOrganizationRequest;
import az.yelo.organizationservice.model.response.GetOrganizationResponse;
import az.yelo.organizationservice.repository.OrganizationRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {
  private final OrganizationRepo organizationRepo;

  @Autowired
  public OrganizationService(OrganizationRepo organizationRepo) {
    this.organizationRepo = organizationRepo;
  }

  @CircuitBreaker(name="organizationService")
  public ResponseEntity<GetOrganizationResponse> getOrganizationById(String organizationId) {
    var organization = this.organizationRepo.findById(organizationId);
    if (organization.isPresent()) {
      GetOrganizationResponse res = new GetOrganizationResponse();
      res.setName(organization.get().getName());
      res.setContactName(organization.get().getContactName());
      res.setContactEmail(organization.get().getContactEmail());
      res.setContactPhone(organization.get().getContactPhone());
      return ResponseEntity.ok(res);
    }
    return ResponseEntity.notFound().build();
  }

  public ResponseEntity<Organization> addOrganization(AddOrganizationRequest organization) {
    Organization newOrg = new Organization();
    newOrg.setId(UUID.randomUUID().toString());
    newOrg.setName(organization.getName());
    newOrg.setContactEmail(organization.getContactEmail());
    newOrg.setContactPhone(organization.getContactPhone());
    newOrg.setContactName(organization.getContactName());
    this.organizationRepo.save(newOrg);
    return ResponseEntity.ok(newOrg);
  }

  public ResponseEntity<Organization> deleteOrganization(String organizationId) {
    var organization = this.organizationRepo.findById(organizationId);
    return organization.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

  }
}
