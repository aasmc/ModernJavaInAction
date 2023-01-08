package ru.aasmc.chapter03;

import ru.aasmc.common.Apple;
import ru.aasmc.common.Color;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class Examples {
    static Map<String, Function<Integer, Apple>> map = new HashMap<>();

    static {
        map.put("apple", Apple::new);
    }

    public static Apple giveMeApple(String name, Integer weight) {
        var function = map.get(name);
        if (function != null) {
            return function.apply(weight);
        }
        return null;
    }

    public static void main(String[] args) {
        TriFunction<Integer, Integer, Integer, RgbColor> colorFactory = RgbColor::new;
        var color = colorFactory.apply(10, 20, 30);
        System.out.println(color);
        Comparator<Apple> c = Comparator.comparing(Apple::getWeight);
        Comparator<Apple> reversed = Comparator.comparing(Apple::getWeight).reversed();
        Predicate<Apple> redApple = (a) -> a.getColor() == Color.RED;
        Predicate<Apple> notRedApple = redApple.negate();
        Predicate<Apple> redAndHeavyOrGreen = redApple
                .and(a -> a.getWeight() > 150)
                .or(a -> Color.GREEN.equals(a.getColor()));

        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = f.andThen(g);
        System.out.println("Function result of andThen ----------- g(f(x)):");
        System.out.println(h.apply(1));

        Function<Integer, Integer> composed = f.compose(g);
        System.out.println("Function result of compose ----------- f(g(x)):");
        System.out.println(composed.apply(1));
    }

    public double integrate(DoubleUnaryOperator f, double a, double b) {
        return (f.applyAsDouble(a) + f.applyAsDouble(b)) * (b - a) / 2.0;
    }

    public double integrate(DoubleFunction<Double> f, double a, double b) {
        return (f.apply(a) + f.apply(b)) * (b - a) / 2.0;
    }
}
