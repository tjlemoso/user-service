package org.acme.com.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

  @Id
  @Column(name = "userId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(name = "createDate")
  private LocalDate createData;

  @Column(name = "name")
  private String name;

  @Column(name = "password")
  private String password;

  @Column(name = "isAdmin")
  private Boolean isAdmin;

}
