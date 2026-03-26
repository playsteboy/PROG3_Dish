package org.example.Controllers;

import org.apache.coyote.BadRequestException;
import org.example.Data.Unit;
import org.example.Entities.StockValue;
import org.example.ExceptionHandlers.EntityNotFoundException;
import org.example.Services.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.Map;

@Controller
public class IngredientController {
    private IngredientService ingredientService;
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }
    @GetMapping("/ingredients")
    public ResponseEntity<?> getIngredients(){
        return ResponseEntity.ok().body(ingredientService.getAllIngredients());
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<?> getIngredient(@PathVariable Integer id){
        try {
            return ResponseEntity.ok().body(ingredientService.getIngredientById(id));
        }catch (Exception e){
            Throwable target = (e.getCause() != null) ? e.getCause() : e;

            if (target instanceof EntityNotFoundException) {
                return ResponseEntity.status(404).body(target.getMessage());
            }
            return ResponseEntity.internalServerError().body(target.getMessage());
        }
    }

    @GetMapping("/ingredients/{id}/stock")
    public ResponseEntity<?> getIngredientStock(@PathVariable Integer id,
                                                @RequestParam(required = false) Instant at,
                                                @RequestParam(required = false) Unit unit)
    {
        try{
            StockValue stock = ingredientService.getStockValueOfIngredient(id, at, unit);
            return ResponseEntity.ok(Map.of(
                    "unité", stock.getUnit(),
                    "valeur", stock.getQuantity()
            ));
        }catch (Exception e){
            Throwable target = (e.getCause() != null) ? e.getCause() : e;

            if (target instanceof BadRequestException) {
                return ResponseEntity.badRequest().body(target.getMessage());
            }
            if (target instanceof EntityNotFoundException) {
                return ResponseEntity.status(404).body(target.getMessage());
            }
            return ResponseEntity.internalServerError().body(target.getMessage());
        }
    }

}
