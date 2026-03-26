package org.example.Repositories;

import org.example.Data.CategoryEnum;
import org.example.Entities.Ingredient;
import org.example.Entities.StockMovement;
import org.example.Entities.StockValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}
