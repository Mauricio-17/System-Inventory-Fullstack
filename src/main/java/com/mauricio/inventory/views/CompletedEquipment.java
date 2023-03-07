package com.mauricio.inventory.views;

import com.mauricio.inventory.equipment.Status;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Immutable
@Table(name = "view_equipment")
@Subselect("select uuid() as id, eq.* from view_equipment eq")
@Getter
@Setter
public class CompletedEquipment implements Serializable {

    @Id
    private String id;
    private Long equipmentId;
    private String sku;
    private String name;
    private String status;
    private int stock;
    private Date createdAt;
    private Date updatedAt;
    private String brandName;
    private String categoryName;
    private Byte shelfRow;
    private String ownerName;
    private String ownerLastName;
    private Long brandId;
    private Long categoryId;
    private Long locationId;
    private Long ownerId;

}
