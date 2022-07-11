package naem.server.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @Operation(summary = "헬로우 테스트1", description = "hello1 api example")
    @GetMapping("/api/hello1")
    public String hello1() {
        return "hello";
    }

    @Operation(summary = "헬로우 테스트2", description = "hello2 api example")
    @GetMapping("/api/hello2")
    public String hello2(@RequestParam String param) {
        return param;
    }
}
