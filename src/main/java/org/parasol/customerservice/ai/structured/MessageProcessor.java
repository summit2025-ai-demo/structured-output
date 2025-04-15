package org.parasol.customerservice.ai.structured;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);

    @Inject
    InformationExtractor informationExtractor;

    public StructuredInformation process(String message) {
        return informationExtractor.extractInformationFrom(message);
    }

}
