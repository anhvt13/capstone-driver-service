package com.driver.repository;

import com.driver.model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "drivers", path = "drivers")
public interface DriverRepository extends JpaRepository<Driver, Integer> {

    @Query("SELECT d FROM Driver d")
    Slice<Driver> findAllAsSlice(Pageable pageable);

    @Query("SELECT d FROM Driver d WHERE d.team = :team")
    Slice<Driver> findByTeamAsSlice(String team, Pageable pageable);

}
