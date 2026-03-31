package org.example.Controllers;

import org.apache.coyote.BadRequestException;
import org.example.DTO.DishCreateRequest;
import org.example.DTO.DishIngredientRequest;
import org.example.Entities.Dish;
import org.example.Entities.DishIngredient;
import org.example.Entities.Ingredient;
import org.example.ExceptionHandlers.EntityNotFoundException;
import org.example.Repositories.DishIngredientRepository;
import org.example.Repositories.DishRepository;
import org.example.Services.DishIngredientService;
import org.example.Services.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class DishController {
    private DishService dishService;
    private DishIngredientRepository dishIngredientRepository;
    private DishRepository  dishRepository;
    public DishController(DishService dishService,  DishIngredientRepository dishIngredientRepository,  DishRepository dishRepository) {
        this.dishService = dishService;
        this.dishIngredientRepository = dishIngredientRepository;
        this.dishRepository = dishRepository;
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateIngredients(
            @PathVariable int id,
            @RequestBody List<DishIngredientRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            return ResponseEntity.status(400)
                    .body("Body is required");
        }

        try {
            dishIngredientRepository.updateDishIngredients(id, requests);
            return ResponseEntity.ok("Updated");

        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(404).body(e.getMessage());
            }
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
@PostMapping("/dishes")
public ResponseEntity<?> create(@RequestBody List<DishCreateRequest> requests) {
    try {
        return ResponseEntity.status(201).body(dishRepository.saveAll(requests));

    }
    catch(Exception e){
            Throwable target = (e.getCause() != null) ? e.getCause() : e;

            if (target instanceof IllegalArgumentException) {
                return ResponseEntity.badRequest().body(target.getMessage());
            }
            return ResponseEntity.internalServerError().body(target.getMessage());
        }
}
    @GetMapping("/dishes")
    public List<Dish> getAll(
            @RequestParam(required = false) Double priceUnder,
            @RequestParam(required = false) Double priceOver,
            @RequestParam(required = false) String name
    ) {
        return dishRepository.findAll(priceUnder, priceOver, name);
    }
}
