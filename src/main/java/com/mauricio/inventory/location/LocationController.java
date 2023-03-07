package com.mauricio.inventory.location;

import com.mauricio.inventory.equipment.Equipment;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(maxAge = 3600, origins = "https://inventory-sys.netlify.app")
@RestController
@RequestMapping("/api/location")
@AllArgsConstructor
public class LocationController {

    private LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations(@RequestHeader(value = "Authorization") String token){
        return locationService.getAllItems(token);
    }
    @GetMapping(value = "/{id}")
    public Location getLocationById(@RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "id") Long id){
        return locationService.getItemById(id, token);
    }
    @GetMapping("/shelf/{id}")
    public List<Location> getLocationByShelfId(@PathVariable(value = "id") Long id, @RequestHeader(value = "Authorization") String token){
        return locationService.getItemsByForeignId(id, token);
    }


    @PostMapping
    public ResponseEntity<Void> addLocation(@Valid @RequestBody Location location, @RequestHeader(value = "Authorization") String token){
        locationService.addItem(location, token);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLocation(@Valid @RequestBody Location location, @PathVariable(value = "id") Long id, @RequestHeader(value = "Authorization") String token){
        locationService.updateItem(location, id, token);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable(value = "id") Long id, @RequestHeader(value = "Authorization") String token){
        locationService.removeItem(id, token);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
