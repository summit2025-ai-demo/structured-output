package org.parasol.customerservice.ai.structured;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;

public class StructuredInformation {

    @JsonProperty(required = true)
    String reason;

    @JsonProperty(required = false)
    String sentiment;

    @JsonProperty(required = false)
    String companyId;

    @JsonProperty(required = false)
    String companyName;

    @JsonProperty(required = false)
    String customerName;

    @JsonProperty(required = false)
    String country;

    @JsonProperty(required = false)
    String emailAddress;

    @JsonProperty(required = false)
    String phone;

    @JsonProperty(required = false)
    String productName;

    boolean escalate;

    public String getReason() {
        return reason;
    }

    public String getSentiment() {
        return sentiment;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCountry() {
        return country;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public String getProductName() {
        return productName;
    }

    public boolean isEscalate() {
        return escalate;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("reason", reason == null || reason.isEmpty() ? null : reason);
        jsonObject.put("sentiment", sentiment == null || sentiment.isEmpty() ? null : sentiment);
        jsonObject.put("company_id", companyId == null || companyId.isEmpty() ? null : companyId);
        jsonObject.put("company_name", companyName == null || companyName.isEmpty() ? null : companyName);
        jsonObject.put("customer_name", customerName == null || customerName.isEmpty() ? null : customerName);
        jsonObject.put("country", country == null || country.isEmpty() ? null : country);
        jsonObject.put("email_address", emailAddress == null || emailAddress.isEmpty() ? null : emailAddress);
        jsonObject.put("phone", phone == null || phone.isEmpty() ? null : phone);
        jsonObject.put("product_name", productName == null || productName.isEmpty() ? null : productName);
        jsonObject.put("escalate", escalate);
        return jsonObject;
    }
}
