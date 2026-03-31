package org.example.Services;

import org.example.Entities.Dish;
import org.example.ExceptionHandlers.EntityNotFoundException;
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
//    public List<Dish> getAllDishes() {
//        return dishRepository.findAll();
//    }
//    public Dish getDishById(int id){
//        Optional<Dish> dish = dishRepository.findById(id);
//        if(dish.isEmpty()){
//            throw new EntityNotFoundException("Dish.id="+id+" is not found");
//        }
//        return dish.get();
//    }

}
