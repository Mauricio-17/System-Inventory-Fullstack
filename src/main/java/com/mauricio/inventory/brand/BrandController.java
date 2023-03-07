package com.mauricio.inventory.brand;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/brand")
@AllArgsConstructor
public class BrandController {

    private BrandService brandService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Brand> getAllBrands(){
        return brandService.getAllItems();
    }

    @PostMapping
    public ResponseEntity<Void> addBrand(@Valid @RequestBody Brand brand ){
        brandService.addItem(brand );
        return ResponseEntity.status(201).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBrand(@Valid @RequestBody Brand brand, @PathVariable(value = "id") Long id){
        brandService.updateItem(brand, id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBrand(@PathVariable(value = "id") Long id ){
        brandService.removeItem(id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
