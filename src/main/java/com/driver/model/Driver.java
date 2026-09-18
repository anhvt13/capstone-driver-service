package com.driver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_driver_team", columnList = "team")})
public class Driver implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank (message = "Name is required")
    private String name;

    @NotBlank (message = "Team is required")
    private String team;
}
