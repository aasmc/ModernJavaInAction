package ru.aasmc.chapter04;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.aasmc.chapter04.Dish.menu;

public class Examples {
    public static void main(String[] args) {
        List<String> lowCaloricDishesNames = menu.stream()
                .filter(d -> d.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(Collectors.toList());
        lowCaloricDishesNames.forEach(System.out::println);

        Map<Dish.Type, List<Dish>> dishesByType =
                menu.stream().collect(Collectors.groupingBy(Dish::getType));
        System.out.println(dishesByType);

        List<String> threeHighCaloricDishNames =
                menu.stream()
                        .filter(d -> d.getCalories() > 300)
                        .map(Dish::getName)
                        .limit(3)
                        .collect(Collectors.toList());
        System.out.println(threeHighCaloricDishNames);
    }
}
