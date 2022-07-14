package naem.server.config;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class SecretYamlReadTest {

    @Autowired
    private SecretYamlRead secretYamlRead;

    @Test
    void test() {
        String port = secretYamlRead.getPort();
        String host = secretYamlRead.getHost();

        log.info("port={}", port);
        log.info("host={}", host);

        Assertions.assertThat(port).isEqualTo("6379");
        Assertions.assertThat(host).isEqualTo("localhost");
    }
}