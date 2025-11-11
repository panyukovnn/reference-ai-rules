package ru.panyukovnn.testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.panyukovnn.testcontainers.service.LinkInfoService;

@SpringBootTest
@ActiveProfiles("test")
public class AbstractTest {

    @Autowired
    protected LinkInfoService linkInfoService;
}