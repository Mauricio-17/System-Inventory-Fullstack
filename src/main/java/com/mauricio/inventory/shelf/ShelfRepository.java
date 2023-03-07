package com.mauricio.inventory.shelf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {

    @Query("" +
            "SELECT CASE WHEN COUNT(s) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Shelf s " +
            "WHERE s.name = ?1 OR s.serial = ?2"
    )
    Boolean existsNameOrSerial(String name, String serial);

    Shelf findByNameAndSerial(String name, String serial);
}
