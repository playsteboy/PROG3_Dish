package org.example.Services;

import org.example.DTO.DishCreateRequest;
import org.example.Entities.Dish;
import org.example.Entities.Ingredient;
import org.example.Exceptions.NotFoundException;
import org.example.Repositories.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DishService {
    private DishRepository dishRepository;
    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> findAllDishes(Double priceUnder, Double priceOver, String name) {
        return dishRepository.findAll(priceUnder,priceOver,name);
    }
    public List<Dish> saveAll(List<DishCreateRequest> requests){
        return dishRepository.saveAll(requests);
    }
    public Dish getById(Integer id){
        Optional<Dish> optionalIngredient = dishRepository.findById(id);
        if (optionalIngredient.isEmpty()) {
            throw new NotFoundException("Dish.id="+id+ "is not found");
        }
        return optionalIngredient.get();
    }
}
