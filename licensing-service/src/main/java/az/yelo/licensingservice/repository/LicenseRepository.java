package az.yelo.licensingservice.repository;

import az.yelo.licensingservice.model.License;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository // So spring will generate dynamic proxy (under the hood) for this class 
public interface LicenseRepository extends CrudRepository<License, String> {
//  List<License> findByOrOrganizationId(String organizationId);

  License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);

  License findByLicenseId(String licenseId);

  List<License> findByOrganizationId(String organizationId);

  List<License> findAll();
}
