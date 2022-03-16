package ru.alexander.convertio.conversions.source.exchangeratesapi;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import static java.nio.file.Files.readString;

@Service
public class ApiKeyVault {
    private static final String API_KEY_LOCATION = "api.key";
    // Yes, I know. I tried to store it in separate secret file, but Heroku doesn't seem to store secrets.
    // I will look for proper solution.
    private static final String API_KEY = "c26e0eef7ddb5c95ff78b406ecf47993";

    @SneakyThrows
    public String getApiKey() {
        if (true) {
            return API_KEY;
        }
        val file = ResourceUtils.getFile("classpath:" + API_KEY_LOCATION);
        return readString(file.toPath());
    }
}
