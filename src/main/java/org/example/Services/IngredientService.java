package org.example.Services;

import org.example.Data.Unit;
import org.example.Entities.Ingredient;
import org.example.Entities.StockValue;
import org.example.Exceptions.BadRequestException;
import org.example.Exceptions.NotFoundException;
import org.example.Repositories.IngredientRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {
    IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    public Ingredient getById(Integer id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (optionalIngredient.isEmpty()) {
            throw new NotFoundException("Ingredient.id=" + id + " is not found");
        }
        return optionalIngredient.get();
    }

    public StockValue getStockValueAt(Integer ingredientId, Instant temporal, Unit unit) {
        if (temporal == null || unit == null) {
            throw new BadRequestException("Either mandatory query parameter `at` or `unit` is not provided.");
        }
        Ingredient ingredient = getById(ingredientId);
        return ingredient.getStockValueAt(temporal, unit);
    }
}