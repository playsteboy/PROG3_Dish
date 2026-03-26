package org.example.Services;

import org.apache.coyote.BadRequestException;
import org.example.Entities.Dish;
import org.example.Entities.DishIngredient;
import org.example.Entities.Ingredient;
import org.example.Repositories.DishIngredientRepository;
import org.example.Repositories.DishRepository;
import org.example.Repositories.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishIngredientService {
    private DishIngredientRepository dishIngredientRepository;
    private IngredientRepository ingredientRepository;
    private DishRepository dishRepository;
    public DishIngredientService(
            DishIngredientRepository dishIngredientRepository,
            IngredientRepository ingredientRepository,
            DishRepository dishRepository) {
        this.dishIngredientRepository = dishIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.dishRepository = dishRepository;
    }

    public void deleteByDishId(Integer dishId){
        dishIngredientRepository.deleteById(dishId);
    }
    public void updateDishIngredient(Dish dish,List<Ingredient> ingredients) {
        try{
            if(ingredients==null || ingredients.isEmpty()){
                throw new BadRequestException("Missing ingredients");
            }
            DishIngredient dishIngredient = new DishIngredient();
            dishIngredient.setDish(dishRepository.getReferenceById(dish.getId()));
            deleteByDishId(dish.getId());
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getId() != null && ingredientRepository.existsById(ingredient.getId())) {
                    dishIngredient.setIngredient(ingredientRepository.getReferenceById(ingredient.getId()));
                    dishIngredientRepository.save(dishIngredient);
                }
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
