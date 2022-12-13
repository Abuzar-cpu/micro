package az.yelo.licensingservice.repository;

import az.yelo.licensingservice.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // So spring will generate dynamic proxy (under the hood) for this class 
public interface LicenseRepository extends CrudRepository<License, String>{
    List<License> findByOrOrganizationId(String organizationId);

    License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);

    License findByLicenseId(String licenseId);
}
