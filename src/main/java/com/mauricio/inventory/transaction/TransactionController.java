package com.mauricio.inventory.transaction;

import com.mauricio.inventory.typeowner.TypeOwner;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @GetMapping
    public List<HashMap<String, Object>> getAllTransactions( ){
        return transactionService.getAllItems( );
    }

    @PostMapping
    public ResponseEntity<Void> addTransaction(@Valid @RequestBody Transaction transaction ){
        transactionService.addItem(transaction );
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable(value = "id") Long id ){
        transactionService.removeItem(id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
