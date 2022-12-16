package az.yelo.licensingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

  private String id;

  private String name;

  private String contactName;

  private String contactEmail;

  private String contactPhone;
}
