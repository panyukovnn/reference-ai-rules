package ru.panyukovnn.httpintegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.panyukovnn.httpintegration.client.IdentityClient;
import ru.panyukovnn.httpintegration.repository.UserRepository;
import ru.panyukovnn.httpintegration.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
public abstract class AbstractTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected UserService userService;
    @Autowired
    protected IdentityClient identityClient;

    @SpyBean
    protected UserRepository userRepository;
}