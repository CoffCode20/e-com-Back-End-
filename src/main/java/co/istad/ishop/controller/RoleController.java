package co.istad.ishop.controller;

import co.istad.ishop.exception.RoleException;
import co.istad.ishop.model.request.CreateRoleDTO;
import co.istad.ishop.model.response.RoleRespondDTO;
import co.istad.ishop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<String> createRole(@RequestBody CreateRoleDTO createRoleDto) throws RoleException {
        roleService.createRole(createRoleDto);
        return ResponseEntity.ok("ROLE_CREATED_SUCCESSFULLY");
    }

    @GetMapping
    public ResponseEntity<Map<String, List<RoleRespondDTO>>> getRoles() {
        return ResponseEntity.ok(Map.of("roles", roleService.getRoles()));
    }
}