package org.example.Controllers;

import org.apache.coyote.BadRequestException;
import org.example.Entities.Dish;
import org.example.Entities.DishIngredient;
import org.example.Entities.Ingredient;
import org.example.ExceptionHandlers.EntityNotFoundException;
import org.example.Services.DishIngredientService;
import org.example.Services.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class DishController {
    private DishService dishService;
    private DishIngredientService dishIngredientService;
    public DishController(DishService dishService,  DishIngredientService dishIngredientService) {
        this.dishService = dishService;
        this.dishIngredientService = dishIngredientService;
    }

    @GetMapping("/dishes")
    public ResponseEntity<?> getAllDishes() {
        return ResponseEntity.ok().body(dishService.getAllDishes());
    }

    @PostMapping("/dishes/{id}/ingredients")
    public ResponseEntity<?> addIngredient(@PathVariable Integer id, @RequestBody List<Ingredient> ingredients) {
        try{
            Dish dishToAdd = dishService.getDishById(id);
            dishIngredientService.updateDishIngredient(dishToAdd,ingredients);
            return ResponseEntity.ok().build();
        }catch(Exception e){
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
