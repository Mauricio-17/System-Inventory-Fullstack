package com.mauricio.inventory.owner;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
public class OwnerController {

    private OwnerService ownerService;

    @GetMapping("/api/owner")
    public List<Owner> getAllOwners( ){
        return ownerService.getAllOwners( );
    }

    @PostMapping("/api/admin/owner")
    public ResponseEntity<Void> addOwner(@Valid @RequestBody Owner owner ){
        ownerService.addItem(owner );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/api/admin/owner/{id}")
    public ResponseEntity<Void> updateOwner(@Valid @RequestBody Owner owner, @PathVariable(value = "id") Long id ){
        ownerService.updateItem(owner, id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/api/admin/owner/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable(value = "id") Long id ){
        ownerService.removeItem(id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
