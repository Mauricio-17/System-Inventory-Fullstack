package com.mauricio.inventory.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    /*@Query("" +
            "SELECT CASE WHEN COUNT(l) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Location l " +
            "WHERE l.row = ?1"
    )
    Boolean existsRow(Byte row);*/
}
