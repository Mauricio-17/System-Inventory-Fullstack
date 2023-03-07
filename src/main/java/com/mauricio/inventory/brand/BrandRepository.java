package com.mauricio.inventory.brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("" +
            "SELECT CASE WHEN COUNT(b) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Brand b " +
            "WHERE b.name = ?1"
    )
    Boolean existsName(String name);

}
