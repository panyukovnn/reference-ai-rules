package ru.panyukovnn.httpintegration;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.request;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class AbstractWireMockTest extends AbstractTest {

    public static final String IDENTITY_USERS_URL = "/identity/api/v1/users";

    @BeforeEach
    public void setUp() {
        WireMock.reset();
    }

    protected void stubResponse(String url, HttpMethod method, String response) {
        stubResponseWithDelay(url, method, response, null, 0);
    }

    protected void stubResponseWithRequestBody(String url, HttpMethod method, String response, String requestBody) {
        stubResponseWithDelay(url, method, response, requestBody, 0);
    }

    protected void stubResponseWithDelay(String url, HttpMethod method, String response, String requestBody, int delay) {
        MappingBuilder request = request(method.name(), urlPathEqualTo(url));

        if (requestBody != null) {
            request.withRequestBody(equalToJson(requestBody));
        }

        stubFor(request.willReturn(aResponse()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withBody(response)
            .withFixedDelay(delay)));
    }

    protected void stub400Response(String url, HttpMethod method) {
        stubFor(request(method.name(), urlPathEqualTo(url))
            .willReturn(badRequest()));
    }

    protected void stub500Response(String url, HttpMethod method) {
        stubFor(request(method.name(), urlPathEqualTo(url))
            .willReturn(serverError()));
    }
}