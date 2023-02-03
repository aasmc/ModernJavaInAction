package ru.aasmc.chapter17.rxjava;

import io.reactivex.Observable;
import ru.aasmc.chapter17.temperature.TempInfo;

import static ru.aasmc.chapter17.rxjava.TempObservable.getTemperature;

public class Main {
    public static void main(String[] args) {
        Observable<TempInfo> observable = getTemperature("New York");
        observable.blockingSubscribe(new TempObserver());
    }
}
