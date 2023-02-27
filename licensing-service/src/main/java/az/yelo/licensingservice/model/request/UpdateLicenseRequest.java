package az.yelo.licensingservice.model.request;

import az.yelo.licensingservice.model.License;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLicenseRequest {
  private String licenseId;

  private String description;

  private String organizationId;

  private String productName;

  private String licenseType;

  private String comment;
}
