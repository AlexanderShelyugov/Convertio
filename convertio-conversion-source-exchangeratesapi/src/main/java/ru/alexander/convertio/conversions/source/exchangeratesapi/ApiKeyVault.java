package ru.alexander.convertio.conversions.source.exchangeratesapi;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.util.ResourceUtils;

import static java.nio.file.Files.readString;

@Deprecated
public class ApiKeyVault {
    private static final String API_KEY_LOCATION = "api.key";
    private static final String API_KEY = "";

    @SneakyThrows
    public String getApiKey() {
        if (true) {
            return API_KEY;
        }
        val file = ResourceUtils.getFile("classpath:" + API_KEY_LOCATION);
        return readString(file.toPath());
    }
}
