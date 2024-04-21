package at.schrer.qrbill.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BaseController {

    @GetMapping
    public String indexNoSlash() {
        return index();
    }

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }
}