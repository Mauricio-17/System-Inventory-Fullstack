package com.mauricio.inventory.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mauricio.inventory.AuditModel;
import com.mauricio.inventory.employee.Employee;
import com.mauricio.inventory.equipment.Equipment;
import com.mauricio.inventory.location.Location;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Transaction extends AuditModel {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = IDENTITY
    )
    @Column(
            name = "id"
    )
    private Long id;
    @Size(max = 200)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipment_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Equipment equipment;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Employee employee;
    @OneToOne
    @JoinColumn(name = "source_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Location source;
    @OneToOne
    @JoinColumn(name = "destination_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Location destination;



}
