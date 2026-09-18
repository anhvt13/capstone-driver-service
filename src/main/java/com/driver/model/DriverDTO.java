package com.driver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverDTO {

    private Integer id;

    private String name;

    private String team;
}
