package org.example.Repositories;
import org.example.Data.CategoryEnum;
import org.example.Entities.Ingredient;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepository {
    Connection connection;
    StockMovementRepository stockMovementRepository;

    public IngredientRepository(Connection connection, StockMovementRepository stockMovementRepository) {
        this.connection = connection;
        this.stockMovementRepository = stockMovementRepository;
    }

    public Optional<Ingredient> findById(Integer id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            select id, name, price, category from ingredient where id = ?
                            """);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("category")));
                ingredient.setPrice(resultSet.getDouble("price"));
                ingredient.setStockMovementList(stockMovementRepository.findAllByIngredientId(id));
                return Optional.of(ingredient);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findAll() {
        List<Ingredient> Ingredients = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            select id, name, price, category from ingredient
                            """);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("id"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("category")));
                ingredient.setPrice(resultSet.getDouble("price"));
                Ingredients.add(ingredient);
            }
            return Ingredients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}