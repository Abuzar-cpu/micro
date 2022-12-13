package az.yelo.organizationservice.repository;

import az.yelo.organizationservice.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepo extends CrudRepository<Organization, String> {

}
