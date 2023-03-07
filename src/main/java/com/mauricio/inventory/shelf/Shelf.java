package com.mauricio.inventory.shelf;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mauricio.inventory.location.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "shelf")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Shelf {

    @Id
    @GeneratedValue(
            strategy = IDENTITY
    )
    @Column(
            name = "id"
    )
    private Long id;
    @Size(max = 20)
    @NotBlank(message = "El campo SERIAL no debe estar vacio")
    @Column(nullable = false, unique = true)
    private String serial;
    @Size(max = 50)
    @NotBlank(message = "El campo NOMBRE no debe estar vacio")
    @Column(nullable = false, unique = true)
    private String name;
    @Size(max = 200)
    private String description;
    /*@OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "shelf"
    )
    //@JsonManagedReference
    private List<Location> locations = new ArrayList<>();*/


}
