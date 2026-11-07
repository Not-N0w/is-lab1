package com.github.not.n0w.islab1.repository;

import com.github.not.n0w.islab1.model.core.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
