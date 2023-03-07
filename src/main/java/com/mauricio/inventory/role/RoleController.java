package com.mauricio.inventory.role;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/auth/role")
public class RoleController {

    private RoleService roleService;

    @GetMapping
    public List<Role> getAllRoles(){
        return roleService.getAllRoles();
    }

    @PostMapping
    public ResponseEntity<Void> addRole(@Valid @RequestBody Role role ){
        roleService.addItem(role );
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateRole(@Valid @RequestBody Role role, @PathVariable(value = "id") Long id ){
        roleService.updateItem(role, id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable(value = "id") Long id ){
        roleService.removeItem(id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
