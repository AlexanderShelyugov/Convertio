package ru.alexander.convertio.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/convertions")
class ConvertionsController {
    @GetMapping
    ResponseEntity<String> convert() {
        return ok().build();
    }
}
