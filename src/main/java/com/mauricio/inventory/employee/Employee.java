package com.mauricio.inventory.employee;

import com.mauricio.inventory.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee extends AuditModel{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = IDENTITY
    )
    @Column(
            name = "id"
    )
    private Long id;
    @NotBlank(message = "Nombre requerido!")
    @Size(max = 80)
    @Column(nullable = false)
    private String name;
    @Size(max = 80)
    private String lastName;
    @Email
    @NotBlank(message = "Email requerido!")
    @Column(nullable = false, unique = true)
    private String email;
    @NotNull(message = "Estado requerido!")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

}
