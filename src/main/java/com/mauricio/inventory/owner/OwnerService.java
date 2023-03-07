package com.mauricio.inventory.owner;

import com.mauricio.inventory.exceptions.BadRequestException;
import com.mauricio.inventory.exceptions.ResourceNotFoundException;
import com.mauricio.inventory.exceptions.UnauthorizedRequestException;
import com.mauricio.inventory.typeowner.TypeOwner;
import com.mauricio.inventory.typeowner.TypeOwnerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OwnerService {

    private OwnerRepository ownerRepository;
    private TypeOwnerRepository typeOwnerRepository;

    public void dataValidation(String dni, String email, Long typeId){

        Optional<TypeOwner> typeOwner = typeOwnerRepository.findById(typeId);
        if (!typeOwner.isPresent()){
            throw new BadRequestException("El ID del Tipo Propietario no está registrado");
        }
    }

    public List<Owner> getAllOwners( ){
        return ownerRepository.findAll();
    }


    public void addItem(Owner owner ){
        if(owner.getTypeOwner() == null){
            log.info("No existe");
            return;
        }
        if (ownerRepository.existsDni(owner.getDni()) || ownerRepository.existsEmail(owner.getEmail())){
            throw new BadRequestException("El DNI o el email ya están registrados");
        }
        dataValidation(owner.getDni(), owner.getEmail(), owner.getTypeOwner().getId());
        ownerRepository.save(owner);
    }

    public void updateItem(Owner owner, Long id ){
        dataValidation(owner.getDni(), owner.getEmail(), owner.getTypeOwner().getId());
        ownerRepository.findById(id).map(owner1 -> {
            owner1.setName(owner.getName());
            owner1.setLastName(owner.getLastName());
            owner1.setCellphoneNumber(owner.getCellphoneNumber());
            owner1.setEmail(owner.getEmail());
            owner1.setDni(owner.getDni());
            owner1.setTypeOwner(owner.getTypeOwner());
            return ownerRepository.save(owner1);
        }).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Propietario con el ID %s no encontrada", id))
        );
    }

    public void removeItem(Long id ){
        Owner foundOwner = ownerRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format("Propietario con el ID %s no encontrada", id))
        );
        ownerRepository.delete(foundOwner);
    }

}
