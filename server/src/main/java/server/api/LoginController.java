package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @PostMapping("/api/admin/setAdminPassword")
    public ResponseEntity<?> setAdminPassword(@RequestBody String password) {
        System.out.println("Admin password: " + password);
        return ResponseEntity.ok().build();
    }
}