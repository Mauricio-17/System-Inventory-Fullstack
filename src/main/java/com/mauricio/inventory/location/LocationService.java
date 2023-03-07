package com.mauricio.inventory.location;

import com.mauricio.inventory.exceptions.BadRequestException;
import com.mauricio.inventory.exceptions.ResourceNotFoundException;
import com.mauricio.inventory.exceptions.UnauthorizedRequestException;
import com.mauricio.inventory.shelf.Shelf;
import com.mauricio.inventory.shelf.ShelfRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationService {

    private LocationRepository locationRepository;
    private ShelfRepository shelfRepository;

    public List<Location> getLocationsByShelfId(Long shelfId){
        List<Location> locations = locationRepository.findAll();
        List<Location> foundLocations = new ArrayList<>();
        for(Location loc: locations){
            if(loc.getId() == shelfId){
                foundLocations.add(loc);
            }
        }
        return foundLocations;
    }

    public void dataValidation(Location location){
        Optional<Shelf> shelf = shelfRepository.findById(location.getShelf().getId());
        if(shelf.isEmpty()){
            throw new BadRequestException("Dato del Rol inválido");
        }
        // this is for validating that a row doesn't encounter at the same shelf
        List<Location> locations = locationRepository.findAll();
        if( !locations.isEmpty()){
            List<Location> foundLocations = new ArrayList<>();
            Long shelfId = shelf.get().getId();
            // filtering by shelfId
            for(Location loc: locations){
                Long foundShelfId = loc.getShelf().getId();
                if(Objects.equals(foundShelfId, shelfId)){
                    if(Objects.equals(loc.getRow(), location.getRow())){
                        if(Objects.equals(location.getId(), loc.getId())){
                            return;
                        }
                        throw new BadRequestException("La fila ya se encuentra registrada en el estante "+ shelf.get().getName());
                    }
                    foundLocations.add(loc);
                }
            }
        }

    }

    public List<Location> getAllItems(String token){
        return locationRepository.findAll();
    }
    public Location getItemById(Long id, String token){
        return locationRepository.findById(id).orElse(null);
    }
    public List<Location> getItemsByForeignId(Long shelfId, String token){
        List<Location> availableLocations = new ArrayList<>();
        Optional<Shelf> foundShelf = shelfRepository.findById(shelfId);

        if (foundShelf.isPresent()){
            List<Location> locations = locationRepository.findAll();
            for(Location loc: locations){
                if(Objects.equals(loc.getShelf().getId(), shelfId) && loc.getStatus() == Status.AVAILABLE){
                    availableLocations.add(loc);
                }
            }
            return availableLocations;
        }
        return new ArrayList<>();
    }

    public void addItem(Location location, String token){
        dataValidation(location);
        try {
            locationRepository.save(location);
        }catch (DataIntegrityViolationException exception){
            exception.getCause();
        }

    }

    public void updateItem(Location location, Long id, String token){
        System.out.println(location);
        dataValidation(location);
        locationRepository.findById(id).map(loc -> {
           loc.setRow(location.getRow());
           loc.setStatus(location.getStatus());
           loc.setShelf(location.getShelf());
           return locationRepository.save(loc);
        }).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Ubicación con el ID %s no encontrada", id))
        );
    }

    public void removeItem(Long id, String token){
        Optional<Location> foundLocation = locationRepository.findById(id);
        if (foundLocation.isEmpty()){
            throw new ResourceNotFoundException(String.format("Ubicación con el ID %s no encontrado", id));
        }
        locationRepository.delete(foundLocation.get());
    }
}
