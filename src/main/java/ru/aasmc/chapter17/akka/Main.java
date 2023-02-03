package ru.aasmc.chapter17.akka;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.AsPublisher;
import akka.stream.javadsl.JavaFlowSupport.Sink;
import akka.stream.javadsl.JavaFlowSupport.Source;
import ru.aasmc.chapter17.temperature.TempInfo;
import ru.aasmc.chapter17.temperature.TempSubscriber;
import ru.aasmc.chapter17.temperature.TempSubscription;

import java.util.concurrent.Flow;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("temp-info");
        ActorMaterializer materializer = ActorMaterializer.create(system);

        Flow.Publisher<TempInfo> publisher = Source.fromPublisher(getTemperatures("New York"))
                .runWith(Sink.asPublisher(AsPublisher.WITH_FANOUT), materializer);
        publisher.subscribe(new TempSubscriber());

        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Flow.Publisher<TempInfo> getTemperatures(String town) {
        return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
    }
}
