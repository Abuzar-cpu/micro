package az.yelo.organizationservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetOrganizationResponse {
  private String name;

  private String contactName;

  private String contactEmail;

  private String contactPhone;
}
