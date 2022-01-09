package com.hoang.springauthentication.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestError {


    public static final String EMPTY = "required";
    public static final String INVALID = "invalid";
    public static final String USED = "used";
    public static final String BLOCKED = "blocked";

    private Map<String, Object> errors;
    private String message;

    public RestError() {

    }

    protected RestError(Map<String, Object> errors, String message) {
        this.errors = errors;
        this.message = message;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Map<String, List<String>> errors) {
        return new Builder(errors);
    }

    public static class Builder {

        private final Map<String, List<String>> errors = new HashMap<>();
        private String message;

        public Builder() {

        }

        public Builder(Map<String, List<String>> errors) {
            this.errors.putAll(errors);
        }

        public Builder addEmptyField(String field) {
            return this.addErrorField(field, Collections.singletonList(EMPTY));
        }

        public Builder addUsedField(String field) {
            return this.addErrorField(field, Collections.singletonList(USED));
        }

        public Builder addInvalidField(String field) {
            return this.addErrorField(field, Collections.singletonList(INVALID));
        }

        public Builder addBlockedField(String field) {
            return this.addErrorField(field, Collections.singletonList(BLOCKED));
        }

        public Builder addMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder addErrorField(String field, List<String> errors) {
            this.errors.put(field, errors);
            return this;
        }

        public RestError build() {

            JSONObject json = new JSONObject();

            for (Map.Entry<String, List<String>> error : errors.entrySet()) {
                String[] keyLevels = error.getKey().split("\\.");
                JSONObject innerJson = json;

                for (int i = 0; i < keyLevels.length; i++) {
                    if (!innerJson.has(keyLevels[i])) {
                        innerJson.put(keyLevels[i], new JSONObject());
                        if (i == keyLevels.length - 1) {
                            innerJson.put(keyLevels[i], error.getValue());
                            break;
                        }
                    }
                    innerJson = innerJson.getJSONObject(keyLevels[i]);
                }
            }

            return new RestError(json.toMap(), message);
        }

    }
}
