package com.mauricio.inventory.owner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    @Query("" +
            "SELECT CASE WHEN COUNT(o) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Owner o " +
            "WHERE o.email = ?1"
    )
    Boolean existsEmail(String email);

    @Query("" +
            "SELECT CASE WHEN COUNT(o) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Owner o " +
            "WHERE o.dni = ?1"
    )
    Boolean existsDni(String dni);
}
