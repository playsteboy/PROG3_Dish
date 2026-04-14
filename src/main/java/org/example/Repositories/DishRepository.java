package org.example.Repositories;

import org.example.DTO.DishCreateRequest;
import org.example.Data.DishTypeEnum;
import org.example.Entities.Dish;
import org.example.Exceptions.NotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DishRepository {
    Connection connection;
    DishRepository(Connection connection) {
        this.connection = connection;
    }
public List<Dish> saveAll(List<DishCreateRequest> requests) {

    List<Dish> result = new ArrayList<>();

    String sql = "INSERT INTO dish(name, dish_type, selling_price) VALUES (?, ?::dish_type, ?)";

    try{

        connection.setAutoCommit(false);

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (DishCreateRequest req : requests) {
                String checkSql = "SELECT 1 FROM dish WHERE name = ?";

                try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
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
                    d.setSellingPrice(req.getPrice());

                    result.add(d);
                }
            }

            connection.commit();

        } catch (Exception e) {
            connection.rollback();
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

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Dish d = new Dish();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                d.setSellingPrice(rs.getDouble("selling_price"));

                result.add(d);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
    public Optional<Dish> findById(Integer id) {
        try(PreparedStatement stmt = connection.prepareStatement("""
select id, name,dish_type, selling_price  from dish where id = ?
""")){
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Dish d = new Dish();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                d.setSellingPrice(rs.getDouble("selling_price"));
                return Optional.of(d);
            }
            throw new NotFoundException("Dish with id " + id + " not found");

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    }

