package com.driver.controller;

import com.driver.exception.CouldNotFoundException;
import com.driver.model.Driver;
import com.driver.model.DriverDTO;
import com.driver.model.SliceDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.driver.service.DriverService;

import java.util.List;

@RestController
public class DriverController {

    private static final Logger log = LoggerFactory.getLogger(DriverController.class);

    @Autowired
    private DriverService driverService;
	
	@GetMapping ("/home")
	public String Home() {
        return "Welcome to Driver micro-service";
	}

    @GetMapping ("/drivers")
    public ResponseEntity<SliceDTO<Driver>> getDrivers(@RequestParam(required = false) String team, @AuthenticationPrincipal Jwt jwt,
                                                       @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        log.info("Request for retrieving list of drivers");
        log.info("Jwt inspector: " + jwt.getClaims().toString());
        Pageable pageable = PageRequest.of(page, size);
        SliceDTO<Driver> driverSlice = driverService.getDrivers(team, pageable);
        return new ResponseEntity<>(driverSlice, HttpStatus.OK);
    }

    @GetMapping ("/driver/{driverId}")
    public ResponseEntity<Driver> getDriver(@PathVariable Integer driverId){
        log.info("Request for retrieving individual driver by Id");
        Driver driver = driverService.getDriverById(driverId).orElseThrow(() -> new CouldNotFoundException("Record not found"));
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_default-m2m-resource-server-tpzpjc/read')")
    @PostMapping ("/driver")
    public ResponseEntity<Driver> saveDriver(@Valid @RequestBody Driver driver) {
        log.info("Request for creating new driver");
        return new ResponseEntity<>(driverService.saveDriver(driver), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_default-m2m-resource-server-tpzpjc/read')")
    @DeleteMapping("/driver/{driverId}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Integer driverId){
        log.info("Request for deleting driver");
        driverService.deleteDriver(driverId);
        return ResponseEntity.noContent().build();
    }
}
