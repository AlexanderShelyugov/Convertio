package ru.alexander.convertio.conversions.source.exchangeratesapi;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import static java.nio.file.Files.readString;

@Service
public class ApiKeyVault {
    private static final String API_KEY_LOCATION = "api.key";

    @SneakyThrows
    public String getApiKey() {
        val file = ResourceUtils.getFile("classpath:" + API_KEY_LOCATION);
        return readString(file.toPath());
    }
}
