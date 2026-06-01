package com.jdc.web2026i;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Evitar que el test intente enlazar al puerto HTTP fijo (conflicto si otra instancia corre)
@SpringBootTest()
class Web2026IApplicationTests {

    @Test
    void contextLoads() {
    }

}
