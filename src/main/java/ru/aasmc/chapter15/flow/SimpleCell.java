package ru.aasmc.chapter15.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

public class SimpleCell implements Flow.Publisher<Integer>, Flow.Subscriber<Integer> {

    public static void main(String[] args) {
        SimpleCell c3 = new SimpleCell("C3");
        SimpleCell c2 = new SimpleCell("C2");
        SimpleCell c1 = new SimpleCell("C1");

        c1.subscribe(c3);

        c1.onNext(10); // Update value of C1 to 10
        c2.onNext(20); // update value of C2 to 20
    }

    private int value = 0;

    private String name;

    private List<Flow.Subscriber<? super Integer>> subscribers = new ArrayList<>();

    public SimpleCell(String name) {
        this.name = name;
    }

    private void notifyAllSubscribers() {
        subscribers.forEach(subscriber -> subscriber.onNext(value));
    }

    public void subscribe(Consumer<? super Integer> onNext) {
        subscribers.add(new Flow.Subscriber<Integer>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
            }

            @Override
            public void onNext(Integer item) {
                onNext.accept(item);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
    }

    @Override
    public void onNext(Integer item) {
        value = item;
        System.out.println(name + ":" + value);
        notifyAllSubscribers();
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void subscribe(Flow.Subscriber<? super Integer> subscriber) {
        subscribers.add(subscriber);
    }
}
