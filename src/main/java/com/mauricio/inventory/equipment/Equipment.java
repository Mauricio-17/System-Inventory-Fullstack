package com.mauricio.inventory.equipment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mauricio.inventory.AuditModel;
import com.mauricio.inventory.brand.Brand;
import com.mauricio.inventory.category.Category;
import com.mauricio.inventory.location.Location;
import com.mauricio.inventory.owner.Owner;
import com.mauricio.inventory.shelf.Shelf;
import com.mauricio.inventory.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "equipment")
public class Equipment extends AuditModel {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = IDENTITY
    )
    @Column(
            name = "id"
    )
    private Long id;

    @Size(max = 15)
    @NotBlank(message = "SKU requerido!")
    @Column(nullable = false, unique = true)
    private String sku;

    @Size(max = 15)
    @NotBlank(message = "El campo NOMBRE no debe estar vacio")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "El campo STOCK no debe estar vacio")
    @Column(nullable = false)
    private int stock;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Brand brand;

    @OneToOne// if it puts Fetch.Lazy may cause an error
    @JoinColumn(name = "location_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //@JsonBackReference the same result
    private Location location; // Unidirectional relationship

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "equipment"
    )
    private List<Transaction> transactions;


}
