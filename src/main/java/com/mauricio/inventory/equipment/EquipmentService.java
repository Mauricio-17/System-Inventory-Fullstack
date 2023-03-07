package com.mauricio.inventory.equipment;

import com.mauricio.inventory.brand.Brand;
import com.mauricio.inventory.brand.BrandRepository;
import com.mauricio.inventory.category.Category;
import com.mauricio.inventory.category.CategoryRepository;
import com.mauricio.inventory.exceptions.BadRequestException;
import com.mauricio.inventory.exceptions.ResourceNotFoundException;
import com.mauricio.inventory.exceptions.UnauthorizedRequestException;
import com.mauricio.inventory.location.Location;
import com.mauricio.inventory.location.LocationRepository;
import com.mauricio.inventory.location.Status;
import com.mauricio.inventory.owner.Owner;
import com.mauricio.inventory.owner.OwnerRepository;
import com.mauricio.inventory.views.CompletedEquipment;
import com.mauricio.inventory.views.CompletedEquipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EquipmentService {

    private EquipmentRepository equipmentRepository;
    private CategoryRepository categoryRepository;
    private OwnerRepository ownerRepository;
    private BrandRepository brandRepository;
    private LocationRepository locationRepository;
    private CompletedEquipmentRepository completedEquipmentRepository;

    public void dataValidation(Equipment equipment){

        //foreignDataValidation(categoryRepository, ownerRepository, brandRepository, locationRepository);
        Optional<Category> category = categoryRepository.findById(equipment.getCategory().getId());
        Optional<Owner> owner = ownerRepository.findById(equipment.getOwner().getId());
        Optional<Brand> brand = brandRepository.findById(equipment.getBrand().getId());
        Optional<Location> location = locationRepository.findById(equipment.getLocation().getId());

        if(!category.isPresent() || !owner.isPresent() || !brand.isPresent() || !location.isPresent()){
            throw new BadRequestException("Error en algún dato de categoría, propietario, " +
                    "marca o ubicación.");
        }
        boolean isAvailable = location.get().getStatus() == Status.AVAILABLE;
        if( !isAvailable){
            throw new BadRequestException("El estante no está disponible");
        }
    }

    public List<Equipment> getAllItems( ){
        return equipmentRepository.findAll( );
    }

    public Page<CompletedEquipment> getAllCompletedEquipments( Pageable page){
        return completedEquipmentRepository.findAll(page);
    }
    public Equipment getItem(Long id ){
        Optional<Equipment> foundEquipment = equipmentRepository.findById(id);
        return foundEquipment.orElse(null);
    }

    public void addItem(Equipment equipment ){
        dataValidation(equipment);
        if(equipmentRepository.existsName(equipment.getName())){
            throw new BadRequestException("El nombre ya está registrado");
        }
        equipmentRepository.save(equipment);
    }

    public void updateItem(Equipment equipment, Long id ){
        dataValidation(equipment);
        equipmentRepository.findById(id).map(eq -> {
            eq.setName(equipment.getName());
            eq.setStock(equipment.getStock());
            eq.setStatus(equipment.getStatus());
            eq.setStock(equipment.getStock());
            eq.setOwner(equipment.getOwner());
            eq.setCategory(equipment.getCategory());
            eq.setBrand(equipment.getBrand());
            eq.setLocation(equipment.getLocation());
            return equipmentRepository.save(eq);
        }).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Equipo con el ID %s no encontrada", id))
        );
    }

    public void removeItem(Long id ){
        Optional<Equipment> foundEquipment = equipmentRepository.findById(id);
        if( !foundEquipment.isPresent()){
            throw new ResourceNotFoundException(String.format("Equipo con el ID %s no encontrada", id));
        }
        equipmentRepository.delete(foundEquipment.get());
    }
}
