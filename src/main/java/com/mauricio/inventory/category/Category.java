package com.mauricio.inventory.category;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mauricio.inventory.equipment.Equipment;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(
            strategy = IDENTITY
    )
    private Long id;
    @Size(max = 80)
    @NotBlank(message = "Nombre requerido!")
    @Column(nullable = false, unique = true)
    private String name;
    @Size(max = 200)
    private String description;
    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL
    )
    private Set<Equipment> equipments;



    public Long getId(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Equipment> getEquipments() {
        return equipments;
    }
    public void setEquipments(List<Equipment> list){
        for(Equipment eq : list){
           this.equipments.add(eq);
        }
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
