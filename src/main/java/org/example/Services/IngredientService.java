package org.example.Services;

import org.apache.coyote.BadRequestException;
import org.example.Data.Unit;
import org.example.Entities.Ingredient;
import org.example.Entities.StockValue;
import org.example.ExceptionHandlers.EntityNotFoundException;
import org.example.Repositories.IngredientRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {
    private IngredientRepository  ingredientRepository;
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }
    public Ingredient getIngredientById(Integer id) {
        try{
            Optional<Ingredient> ingredientToFind = ingredientRepository.findById(id);
            if (ingredientToFind.isEmpty()) {
                throw new EntityNotFoundException("Ingredient.id="+id+" is not found");
            }
            return  ingredientToFind.get();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public StockValue getStockValueOfIngredient(Integer id, Instant instant, Unit unit) {
        try{
            if ( unit == null || instant == null) {
                throw new BadRequestException("Either mandatory query parameter `at` or \n" +
                        "`unit` is not provided.");
            }
            Ingredient ingredient = getIngredientById(id);
            return ingredient.getStockValueAt(instant);
        }catch(Exception e){
            Throwable target = (e.getCause() != null) ? e.getCause() : e;
            if (target instanceof EntityNotFoundException) {
                try{
                    throw new EntityNotFoundException(target.getMessage());
                }catch(Exception ex){
                    throw new RuntimeException(target);
                }

            }
            throw new RuntimeException(target);
        }

    }
}
