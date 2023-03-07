package com.mauricio.inventory.owner;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mauricio.inventory.AuditModel;
import com.mauricio.inventory.equipment.Equipment;
import com.mauricio.inventory.typeowner.TypeOwner;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "owner")
@AllArgsConstructor
@NoArgsConstructor
public class Owner extends AuditModel{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(
            strategy = IDENTITY
    )
    @Column(
            name = "id"
    )
    private Long id;
    @NotBlank(message = "El campo NOMBRE no debe estar vacio")
    @Size(max = 80)
    @Column(nullable = false)
    private String name;
    @Size(max = 80)
    private String lastName;
    @Size(max = 15)
    private String cellphoneNumber;
    @NotBlank(message = "El campo EMAIL no debe estar vacio")
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "El campo DNI no debe estar vacio")
    @Size(max = 10)
    @Column(nullable = false, unique = true)
    private String dni;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TypeOwner typeOwner;
    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY
    )
    private Set<Equipment> equipments = new HashSet<>();


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public TypeOwner getTypeOwner() {
        return typeOwner;
    }

    public void setTypeOwner(TypeOwner typeOwner) {
        this.typeOwner = typeOwner;
    }

    public Set<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Set<Equipment> equipments) {
        for(Equipment eq : equipments){
            this.equipments.add(eq);
        }
    }
}
