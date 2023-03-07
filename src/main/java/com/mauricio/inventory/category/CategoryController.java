package com.mauricio.inventory.category;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping

    public List<Category> getAllCategories(){
        return categoryService.getAllItems();
    }
    @PostMapping
    public ResponseEntity<Void> addCategory(@Valid @RequestBody Category category){
        categoryService.addItem(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@Valid @RequestBody Category category, @PathVariable(value = "id") Long id ){
        categoryService.updateItem(category, id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(value = "id") Long id ){
        categoryService.removeItem(id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
