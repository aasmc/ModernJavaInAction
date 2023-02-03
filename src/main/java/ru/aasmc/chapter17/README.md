# Reactive Systems

Java 9 adds one new class for reactive programming: java.util.concurrent.Flow.
The Flow class contains four nested interfaces to express the publish-subscribe model of reactive programming as standardized by the Reactive Streams project:

- Publisher
- Subscriber
- Subscription
- Processor

```java
@FunctionalInterface
public interface Publisher<T> {
    void subscribe(Subscriber<? super T> s);
}


public interface Subscriber<T> {
    void onSubscribe(Subscription s);
    void onNext(T t);
    void onError(Throwable t);
    void onComplete();
}

public interface Subscription {
    void request(long n);
    void cancel();
}

public interface Processor<T, R> extends Subscriber<T>, Publisher<R> { }
```


Those events have to be published (and the corresponding methods invoked) strictly following the sequence defined by this protocol:

```text
onSubscribe onNext* (onError | onComplete)?
```

Processor represents a transformation stage of the events processed through the reactive stream.
When receiving an error, the Processor can choose to recover from it (and then consider the 
Subscription to be canceled) or immediately propagate the onError signal to its Subscriber(s).
The Processor should also cancel its upstream Sub- scription when its last Subscriber cancels 
its Subscription to propagate the cancella- tion signal (even though this cancellation isn’t 
strictly required by the specification).