package ru.aasmc.chapter17.temperature;

import java.util.concurrent.Flow;

/**
 * A processor that transforms ont TempInfo (Fahrenheit) into another TempInfo (Celsius).
 */
public class TempProcessor implements Flow.Processor<TempInfo, TempInfo> {
    private Flow.Subscriber<? super TempInfo> subscriber;


    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(TempInfo item) {
        subscriber.onNext(new TempInfo(
                item.getTown(),
                (item.getTemp() - 32) * 5 / 9
        ));
    }

    @Override
    public void onError(Throwable throwable) {
        subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        subscriber.onComplete();
    }


    @Override
    public void subscribe(Flow.Subscriber<? super TempInfo> subscriber) {
        this.subscriber = subscriber;
    }
}
