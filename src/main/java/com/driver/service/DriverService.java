package com.driver.service;

import com.driver.model.Driver;
import com.driver.model.DriverDTO;
import com.driver.model.SliceDTO;
import com.driver.repository.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DriverService {

    private static final Logger log = LoggerFactory.getLogger(DriverService.class);

    @Autowired
    private DriverRepository driverRepository;

    public SliceDTO<Driver> getDrivers(String team, Pageable pageable) {
        if (Objects.isNull(team)) {
            Slice<Driver> driverSlice = driverRepository.findAllAsSlice(pageable);
            return new SliceDTO<Driver>(driverSlice.getContent(), driverSlice.getNumber(), driverSlice.getSize(), driverSlice.hasNext());
        }
        Slice<Driver> driverSlice = driverRepository.findByTeamAsSlice(team, pageable);
        return new SliceDTO<Driver>(driverSlice.getContent(), driverSlice.getNumber(), driverSlice.getSize(), driverSlice.hasNext());
    }

    @Cacheable(value = "driver", key = "#driverId")
    public Optional<Driver> getDriverById(Integer driverId) {
        log.info("Cache miss. Start querying from the database.");
        return driverRepository.findById(driverId);
    }

    public Driver saveDriver(Driver driver){
        return driverRepository.saveAndFlush(driver);
    }

    @CacheEvict(value = "driver", key = "#driverId")
    public void deleteDriver(Integer driverId) {
        log.info("Cache hit. Start removing from cache");
        driverRepository.deleteById(driverId);
    }

    private DriverDTO toDriverDTO(Driver driver) {
        return new DriverDTO(driver.getId(), driver.getName(), driver.getTeam());
    }
}
