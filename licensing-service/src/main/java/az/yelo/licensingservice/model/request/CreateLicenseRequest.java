package az.yelo.licensingservice.model.request;

import az.yelo.licensingservice.model.response.CreateLicenseResponse;
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
public class CreateLicenseRequest {
  private String description;
  private String organizationId;
  private String productName;
  private String licenseType;
  private String comment;
}
