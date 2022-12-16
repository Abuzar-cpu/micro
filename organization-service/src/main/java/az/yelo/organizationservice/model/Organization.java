package az.yelo.organizationservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "contact_name")
  private String contactName;

  @Column(name = "contact_email")
  private String contactEmail;

  @Column(name = "contact_phone")
  private String contactPhone;
}
