package az.yelo.licensingservice.model.response;

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
public class UpdateLicenseResponse {
  private String licenseId;
  private String description;
  private String organizationId;
  private String productName;
  private String licenseType;
  private String comment;
}
