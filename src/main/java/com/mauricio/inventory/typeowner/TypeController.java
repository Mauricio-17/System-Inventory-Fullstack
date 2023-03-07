package com.mauricio.inventory.typeowner;

import com.mauricio.inventory.shelf.Shelf;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
public class TypeController {

    private TypeService typeService;

    public String getHome(){
        return "This is a random";
    }

    @GetMapping("/api/type")
    public List<TypeOwner> getAllTypes(){
        return typeService.getAllTypes();
    }

    @PostMapping("/api/admin/type")
    public ResponseEntity<Void> addTypeOwner(@Valid @RequestBody TypeOwner typeOwner){
        typeService.addType(typeOwner);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PutMapping("/api/admin/type/{id}")
    public ResponseEntity<Void> updateType(@Valid @RequestBody TypeOwner type, @PathVariable(value = "id") Long id ){
        typeService.updateItem(type, id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/api/admin/type/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable(value = "id") Long id ){
        typeService.removeItem(id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
