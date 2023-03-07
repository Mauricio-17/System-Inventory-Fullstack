package com.mauricio.inventory.typeowner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOwnerRepository extends JpaRepository<TypeOwner, Long> {

    @Query("" +
            "SELECT CASE WHEN COUNT(to) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM TypeOwner to " +
            "WHERE to.name = ?1"
    )
    Boolean existsName(String name);
}
