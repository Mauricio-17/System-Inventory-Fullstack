package com.mauricio.inventory.category;

import com.mauricio.inventory.exceptions.BadRequestException;
import com.mauricio.inventory.exceptions.ConflictingRequestException;
import com.mauricio.inventory.exceptions.ResourceNotFoundException;
import com.mauricio.inventory.exceptions.UnauthorizedRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;

    public List<Category> getAllItems(){
        return categoryRepository.findAll();
    }

    public void addItem(Category category){
        String name = category.getName();
        if (categoryRepository.existsName(name)){
            throw new BadRequestException(String.format("El nombre {%s} ya existe",name));
        }
        categoryRepository.save(category);
    }

    public void updateItem(Category foundCategory, Long id ){

        categoryRepository.findById(id).map(category -> {
            category.setName(foundCategory.getName());
            category.setDescription(foundCategory.getDescription());
            return categoryRepository.save(category);
        }).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Categoria con el ID %s no encontrada", id))
        );
    }

    public void removeItem(Long id ){

        Category foundCategory = categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Categoria con el ID %s no encontrada", id))
        );
        if ( !foundCategory.getEquipments().isEmpty()){
            throw new ConflictingRequestException("Existe información de equipos que dependen de éste.");
        }
        categoryRepository.delete(foundCategory);
    }
}
