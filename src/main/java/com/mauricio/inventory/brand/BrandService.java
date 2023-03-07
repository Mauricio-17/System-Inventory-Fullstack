package com.mauricio.inventory.brand;

import com.mauricio.inventory.exceptions.BadRequestException;
import com.mauricio.inventory.exceptions.ResourceNotFoundException;
import com.mauricio.inventory.exceptions.UnauthorizedRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BrandService {

    private BrandRepository brandRepository;

    private void dataValidation(String name){
        if(brandRepository.existsName(name)){
            throw new BadRequestException(String.format("El nombre {%s} ya existe",name));
        }
    }

    public List<Brand> getAllItems(){
        return brandRepository.findAll();
    }

    public Brand getItem(Long id ){
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("MArca con el ID %s no encontrada", id))
        );
        return brand;
    }

    public void addItem(Brand brand ){
        dataValidation(brand.getName());
        brandRepository.save(brand);
    }

    public void updateItem(Brand brand, Long id ){
        brandRepository.findById(id).map(br -> {
                br.setName(brand.getName());
                br.setCountry(br.getCountry());
                return brandRepository.save(br);
        }).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Marca con el ID %s no encontrada", id))
        );
    }

    public void removeItem(Long id ){
        Optional<Brand> foundBrand = brandRepository.findById(id);
        if (foundBrand.isEmpty()){
            throw new ResourceNotFoundException(String.format("Marca con el ID %s no encontrada", id));
        }
        brandRepository.delete(foundBrand.get());
    }
}
