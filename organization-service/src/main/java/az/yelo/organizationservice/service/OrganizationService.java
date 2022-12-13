package az.yelo.organizationservice.service;

import az.yelo.organizationservice.model.Organization;
import az.yelo.organizationservice.repository.OrganizationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {
  private final OrganizationRepo organizationRepo;

  @Autowired
  public OrganizationService (OrganizationRepo organizationRepo) {
    this.organizationRepo = organizationRepo;
  }

  public Organization getOrganizationById(String organizationId) {
    return this.organizationRepo.findById(organizationId).isEmpty() ? null : this.organizationRepo.findById(organizationId).get();
  }

  public Organization addOrganization(Organization organization) {
    return this.organizationRepo.save(organization);
  }
}
