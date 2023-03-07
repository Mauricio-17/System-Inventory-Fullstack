package com.mauricio.inventory.location;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mauricio.inventory.shelf.Shelf;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "location")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(
            strategy = IDENTITY
    )
    @Column(
            name = "id"
    )
    private Long id;

    //@NotBlank  it's unable to use with Byte
    @NotNull(message = "El campo Fila no debe estar vacio")
    @Column(name = "shelf_row", nullable = false)
    private Byte row;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "shelf_id")
    //@JsonBackReference
    private Shelf shelf;


}
