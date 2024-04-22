package at.schrer.qrbill.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ConditionalOnProperty(name = "api.opendata.host.key")
@Repository
public class OpenDataHostClient {
    private static final String BASE_URL = "https://api.opendata.host/1.0/";
    private static final String UID_VALIDATE_URL_PATTERN = BASE_URL + "vat-id/validate?vat-id=%s";

    private final RestTemplate restTemplate;

    public OpenDataHostClient(@Value("${api.opendata.host.key}") String apiKey) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON));
        converter.setDefaultCharset(StandardCharsets.ISO_8859_1);
        this.restTemplate = new RestTemplateBuilder()
                .basicAuthentication(apiKey, "")
                .additionalMessageConverters(converter)
                .build();
    }

    public Optional<ValidateResponse> fetchCompanyByUid(String uid) {
        try {
            ValidateResponse response = restTemplate.getForObject(String.format(UID_VALIDATE_URL_PATTERN, uid), ValidateResponse.class);
            if (response == null || !Objects.equals(0, response.getErrorCode())) {
                return Optional.empty();
            }
            return Optional.of(response);
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    @Getter
    @Setter
    public static class ValidateResponse {
        @JsonProperty("reg-no")
        private String registerNumber;
        @JsonProperty("reg-no-class")
        private String registeNameClass;
        private String name;
        private String size;
        private Integer errorCode;
        private String errorMessage;
    }
}
