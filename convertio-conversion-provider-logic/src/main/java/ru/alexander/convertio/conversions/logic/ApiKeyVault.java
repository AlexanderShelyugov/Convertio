package ru.alexander.convertio.conversions.logic;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Service;

import static java.nio.file.Files.readString;
import static org.springframework.util.ResourceUtils.getFile;

@Service
public class ApiKeyVault {
    private static final String API_KEY_LOCATION = "api.key";

    @SneakyThrows
    public String getApiKey() {
        val file = getFile("classpath:" + API_KEY_LOCATION);
        return readString(file.toPath());
    }
}
