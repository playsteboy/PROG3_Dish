package org.example.Services;


import org.example.DTO.DishIngredientRequest;

import org.example.Repositories.DishIngredientRepository;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishIngredientService {
    private IngredientService ingredientService;
    private DishService dishdishService;
    private DishIngredientRepository dishIngredientRepository;
    public DishIngredientService(DishIngredientRepository dishIngredientRepository,
                                 IngredientService ingredientService,
                                 DishService dishRepository) {
        this.dishIngredientRepository = dishIngredientRepository;
        this.ingredientService = ingredientService;
        this.dishdishService = dishRepository;
    }

    public void updateDishIngredient(int dishId, List<DishIngredientRequest> requests) {
        dishdishService.getById(dishId);
        for (DishIngredientRequest dishIngredientRequest : requests) {
            ingredientService.getById(dishIngredientRequest.getIdIngredient());
        }
        dishIngredientRepository.updateDishIngredients(dishId, requests);
    }

}
