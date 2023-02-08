package az.yelo.licensingservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "licenses")
public class License extends RepresentationModel<License> {

  @Id
  @Column(name = "license_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String licenseId;

  private String description;

  @Column(name = "organization_id", nullable = false)
  private String organizationId;

  @Column(name = "product_name", nullable = false)
  private String productName;

  @Column(name = "license_type", nullable = false)
  private String licenseType;

  @Column(name = "comment")
  private String comment;

  public License withComment(String comment) {
    this.comment = comment;
    return this;
  }

  public License withId(String id) {
    this.licenseId = id;
    return this;
  }

  public License withOrganizationId(String organizationId) {
    this.organizationId = organizationId;
    return this;
  }

  public License withDescription(String description) {
    this.description = description;
    return this;
  }

  public License withProductName(String productName) {
    this.productName = productName;
    return this;
  }

  public License withLicenseType(String licenseType) {
    this.licenseType = licenseType;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
