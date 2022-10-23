package com.example.recipea.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Mahdi Sharifi
 */

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    @JsonProperty("error_code")
    private int errorCode;

    @NotBlank(message = "#message is mandatory to every response")
    private String message = "Success";
    private String details;
    @JsonIgnore
    private HttpStatus httpStatus=HttpStatus.OK;
    private final Date timestamp = new Date();
    private List<T> payload = new ArrayList<>();

    Map<String, String> errors;

    public ResponseDto() {
    }

    public ResponseDto(List<T> payload) {
        this.payload = payload;
    }

    public static <T> ResponseDto.ResponseDtoBuilder<T> builder() {
        return new ResponseDtoBuilder<T>();
    }

    public static class ResponseDtoBuilder<T> {
        private int errorCode;
        private String message="Success";
        private String details;
        private HttpStatus httpStatus;
//        private final Instant timestamp = Instant.now();
        private List<T> payload;
        private Map<String, String> errors;

        ResponseDtoBuilder() {
        }

        @JsonProperty("error_code")
        public ResponseDto.ResponseDtoBuilder<T> errorCode(final int errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ResponseDto.ResponseDtoBuilder<T> message(final String message) {
            this.message = message;
            return this;
        }

        public ResponseDto.ResponseDtoBuilder<T> details(final String details) {
            this.details = details;
            return this;
        }
        public ResponseDto.ResponseDtoBuilder<T> errors(final Map<String, String> errors) {
            this.errors = errors;
            return this;
        }

        @JsonIgnore
        public ResponseDto.ResponseDtoBuilder<T> httpStatus(final HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ResponseDto.ResponseDtoBuilder<T> payload(final List<T> payload) {
            this.payload = payload;
            return this;
        }

        public ResponseDto<T> build() {
            return new ResponseDto<>(this.errorCode, this.message, this.details, this.httpStatus, this.payload,this.errors);
        }

    }
}
