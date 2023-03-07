package com.mauricio.inventory.transaction;

import com.mauricio.inventory.exceptions.BadRequestException;
import com.mauricio.inventory.exceptions.ResourceNotFoundException;
import com.mauricio.inventory.exceptions.UnauthorizedRequestException;
import com.mauricio.inventory.location.Location;
import com.mauricio.inventory.location.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;
    private LocationRepository locationRepository;

    public void dataValidation(Transaction transaction){
        Optional<Location> source = locationRepository.findById(transaction.getSource().getId());
        Optional<Location> destination = locationRepository.findById(transaction.getDestination().getId());
        if(source.isEmpty() || destination.isEmpty()){
            throw new BadRequestException("Dato de la ubicación fuente o destino inválido");
        }
    }

    public List<HashMap<String, Object>> getAllItems( ){
        List<HashMap<String, Object>> result = new ArrayList<>();
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction tr: transactions) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("id", tr.getId());
            item.put("description", tr.getDescription());
            item.put("equipmentName", tr.getEquipment().getName()); // it can't receive an
            // object instance
            item.put("equipmentId", tr.getEquipment().getId());
            item.put("employeeName", tr.getEmployee().getName());
            item.put("employeeId", tr.getEmployee().getId());
            item.put("source", tr.getSource());
            item.put("destination", tr.getDestination());
            item.put("createdAt", tr.getCreatedTime());
            result.add(item);
        }
        return result;
    }

    public void addItem(Transaction transaction){
        dataValidation(transaction);
        transactionRepository.save(transaction);
    }

    public void removeItem(Long id ){
        Optional<Transaction> foundTransaction = transactionRepository.findById(id);
        if (foundTransaction.isEmpty()){
            throw new ResourceNotFoundException(String.format("Transacción con el ID %s no encontrado", id));
        }
        transactionRepository.delete(foundTransaction.get());
    }
}
