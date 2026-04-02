package org.example.Controllers;

import org.example.Data.Unit;
import org.example.Entities.Ingredient;
import org.example.Entities.StockValue;
import org.example.Exceptions.BadRequestException;
import org.example.Exceptions.NotFoundException;
import org.example.Services.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class IngredientController {
    IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<?> getIngredients() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ingredientService.findAll());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable(name = "id") Integer ingredientId) {
        try {
            Ingredient ingredient = ingredientService.getById(ingredientId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ingredient);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/ingredients/{id}/stock")
    public ResponseEntity<?> getIngredientStockById(@PathVariable(name = "id") Integer ingredientId,
                                                    @RequestParam(name = "at", required = false) Instant temporal,
                                                    @RequestParam(name = "unit", required = false) Unit unit) {

        try {
            StockValue stockValue = ingredientService.getStockValueAt(ingredientId, temporal, unit);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(stockValue);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

}