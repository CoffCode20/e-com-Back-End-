package co.istad.ishop.controller;


import co.istad.ishop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserToAdminController {

    private final UserService userService;

    @PutMapping("/{username}/promote-admin")
    public ResponseEntity<?> promoteUser(@PathVariable String username) {
        userService.promoteUserToAdmin(username);
        return ResponseEntity.ok("User " + username + " has been promoted to ADMIN.");
    }
}
