package org.example.Repositories;

import org.apache.coyote.BadRequestException;
import org.example.DTO.DishCreateRequest;
import org.example.Data.DishTypeEnum;
import org.example.Entities.Dish;
import org.example.Entities.DishIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishRepository {
    private final DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
public List<Dish> saveAll(List<DishCreateRequest> requests) {

    List<Dish> result = new ArrayList<>();

    String sql = "INSERT INTO dish(name, dish_type, selling_price) VALUES (?, ?::dish_type, ?)";

    try (Connection conn = dataSource.getConnection()) {

        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (DishCreateRequest req : requests) {
                String checkSql = "SELECT 1 FROM dish WHERE name = ?";

                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, req.getName());

                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        throw new IllegalArgumentException(
                                "Dish.name=" + req.getName() + " already exists"
                        );
                    }
                }
                stmt.setString(1, req.getName());
                stmt.setString(2, req.getDishType().name());
                stmt.setDouble(3, req.getPrice());

                try {
                    stmt.executeUpdate();

                } catch (SQLException e) {
                    if ("23505".equals(e.getSQLState())) {
                        throw new IllegalArgumentException(
                                "Dish.name=" + req.getName() + " already exists"
                        );
                    }
                    throw e;
                }

                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    Dish d = new Dish();
                    d.setId(keys.getInt(1));
                    d.setName(req.getName());
                    d.setDishType(req.getDishType());
                    d.setSelling_price(req.getPrice());

                    result.add(d);
                }
            }

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        }

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    return result;
}
    public List<Dish> findAll(Double priceUnder, Double priceOver, String name) {

        List<Dish> result = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT id,name,dish_type,selling_price FROM dish WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (priceUnder != null) {
            sql.append(" AND selling_price < ?");
            params.add(priceUnder);
        }

        if (priceOver != null) {
            sql.append(" AND selling_price > ?");
            params.add(priceOver);
        }

        if (name != null && !name.isEmpty()) {
            sql.append(" AND name ILIKE ?");
            params.add("%" + name + "%");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Dish d = new Dish();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                d.setSelling_price(rs.getDouble("selling_price"));

                result.add(d);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
    }

