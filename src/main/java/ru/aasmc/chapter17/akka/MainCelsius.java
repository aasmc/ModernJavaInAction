package ru.aasmc.chapter17.akka;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.AsPublisher;
import akka.stream.javadsl.JavaFlowSupport;
import ru.aasmc.chapter17.temperature.TempInfo;
import ru.aasmc.chapter17.temperature.TempProcessor;
import ru.aasmc.chapter17.temperature.TempSubscriber;
import ru.aasmc.chapter17.temperature.TempSubscription;

import java.util.concurrent.Flow;

public class MainCelsius {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("temp-info");
        Materializer materializer = ActorMaterializer.create(system);

        Flow.Publisher<TempInfo> publisher =
                JavaFlowSupport.Source.fromPublisher(getCelsiusTemperatures("New York"))
                        .runWith(JavaFlowSupport.Sink.asPublisher(AsPublisher.WITH_FANOUT), materializer);
        publisher.subscribe(new TempSubscriber());

        try {
            Thread.sleep(10000L);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Flow.Publisher<TempInfo> getCelsiusTemperatures(String town) {
        return subscriber -> {
            TempProcessor processor = new TempProcessor();
            processor.subscribe(subscriber);
            processor.onSubscribe(new TempSubscription(processor, town));
        };
    }

}
