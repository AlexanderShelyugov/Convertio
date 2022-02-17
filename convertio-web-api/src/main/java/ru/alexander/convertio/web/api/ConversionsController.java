package ru.alexander.convertio.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/conversions", produces = {APPLICATION_JSON_VALUE})
class ConversionsController {
    @GetMapping
    ResponseEntity<String> convert() {
        return ok().body("Ok, here are your conversions");
    }
}
