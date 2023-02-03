package ru.aasmc.chapter17.rxjava;

import io.reactivex.Observable;
import ru.aasmc.chapter17.temperature.TempInfo;

import static ru.aasmc.chapter17.rxjava.TempObservable.getCelsiusTemperatures;

public class MainCelsius {
    public static void main(String[] args) {
        Observable<TempInfo> observable = getCelsiusTemperatures("New York", "Chicago", "San Francisco");
        observable.blockingSubscribe(new TempObserver());
    }
}
