package com.mauricio.inventory.brand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "brand")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Brand {

    @Id
    @GeneratedValue(
            strategy = IDENTITY
    )
    @Column(
            name = "id"
    )
    private Long id;

    @Size(max = 80)
    @NotBlank(message = "El campo NOMBRE no debe estar vacio")
    @Column(nullable = false, unique = true)
    private String name;
    @Size(max = 30, message = "Nombre muy largo para un país")
    private String country;



}
