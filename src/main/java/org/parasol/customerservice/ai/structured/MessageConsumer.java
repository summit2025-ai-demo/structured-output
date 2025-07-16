package org.parasol.customerservice.ai.structured;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.reactive.messaging.kafka.Record;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

@ApplicationScoped
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @Inject
    MessageProcessor messageProcessor;

    @Inject
    StructuredMessageEmitter structuredMessageEmitter;

    @Inject
    ErrorEventEmitter errorEventEmitter;

    @Incoming("message")
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public Uni<Void> consume(Record<String, String> message) {
        LOGGER.info("Received message:{}", message.value());
        return Uni.createFrom().item(message).emitOn(Infrastructure.getDefaultWorkerPool())
                .onItem().transform(m -> {
                    JsonObject json = new JsonObject(m.value());
                    String content = json.getString("content");
                    StructuredInformation si = messageProcessor.process(content);
                    JsonObject siJson = si.toJsonObject();
                    LOGGER.info("Structured Information: {}", siJson.encode());
                    json.put("structured", siJson);
                    return json;
                })
                .onItem().invoke(j -> structuredMessageEmitter.emit(message.key(), j.encode()))
                .onItem().transformToUni(m -> Uni.createFrom().voidItem())
                .onFailure().recoverWithItem(t -> {
                    LOGGER.error("Error while processing Message", t);
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    t.printStackTrace(pw);
                    JsonObject error = new JsonObject();
                    error.put("source", "structured-output");
                    error.put("error", t.getMessage());
                    error.put("stack_trace", sw.toString());
                    if (t instanceof DecodeException) {
                        error.put("message", message.value());
                        errorEventEmitter.emit(message.key(), error.encode());
                    } else {
                        JsonObject json = new JsonObject(message.value());
                        JsonArray errors = json.getJsonArray("errors");
                        if (errors == null) {
                            errors = new JsonArray();
                        }
                        errors.add(error);
                        json.put("errors", errors);
                        errorEventEmitter.emit(message.key(), json.encode());
                    }
                    return null;
                });
    }
}
