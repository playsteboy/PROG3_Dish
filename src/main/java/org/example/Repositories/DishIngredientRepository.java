package org.example.Repositories;


import org.example.DTO.DishIngredientRequest;
import org.example.Entities.Dish;
import org.example.Entities.DishIngredient;
import org.example.Entities.Ingredient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class DishIngredientRepository {
    private final DataSource dataSource;

    public DishIngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void updateDishIngredients(int dishId, List<DishIngredientRequest> requests) {

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT 1 FROM dish WHERE id = ?")) {

                stmt.setInt(1, dishId);
                ResultSet rs = stmt.executeQuery();

                if (!rs.next()) {
                    throw new RuntimeException("Dish.id=" + dishId + " is not found");
                }
            }
            Set<Integer> existing = new HashSet<>();

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id_ingredient FROM dishingredient WHERE id_dish = ?")) {

                stmt.setInt(1, dishId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    existing.add(rs.getInt("id_ingredient"));
                }
            }
            Set<Integer> incoming = new HashSet<>();

            for (DishIngredientRequest req : requests) {
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT 1 FROM ingredient WHERE id = ?")) {

                    stmt.setInt(1, req.getIdIngredient());
                    ResultSet rs = stmt.executeQuery();

                    if (!rs.next()) continue;
                }

                incoming.add(req.getIdIngredient());
                if (!existing.contains(req.getIdIngredient())) {

                    try (PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO dishingredient(id_dish, id_ingredient, quantity_required, unit) VALUES (?, ?, ?, ?)")) {

                        stmt.setInt(1, dishId);
                        stmt.setInt(2, req.getIdIngredient());
                        stmt.setDouble(3, req.getQuantity());
                        stmt.setString(4, req.getUnit().name());

                        stmt.executeUpdate();
                    }
                }
            }
            for (Integer oldId : existing) {
                if (!incoming.contains(oldId)) {

                    try (PreparedStatement stmt = conn.prepareStatement(
                            "DELETE FROM dishingredient WHERE id_dish = ? AND id_ingredient = ?")) {

                        stmt.setInt(1, dishId);
                        stmt.setInt(2, oldId);

                        stmt.executeUpdate();
                    }
                }
            }

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
