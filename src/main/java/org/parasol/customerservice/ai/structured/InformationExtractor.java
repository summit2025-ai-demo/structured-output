package org.parasol.customerservice.ai.structured;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService()
@ApplicationScoped
public interface InformationExtractor {

    @SystemMessage("Extract the customer support email information. Do not make up information. If a piece of information is not present, leave the field in the response blank")
    StructuredInformation extractInformationFrom(String text);
}
