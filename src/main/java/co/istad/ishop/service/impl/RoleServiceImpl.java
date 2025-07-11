package co.istad.ishop.service.impl;

import co.istad.ishop.entities.Role;
import co.istad.ishop.exception.ApiException;
import co.istad.ishop.exception.RoleException;
import co.istad.ishop.mapper.RoleMapper;
import co.istad.ishop.model.request.CreateRoleDTO;
import co.istad.ishop.model.response.RoleRespondDTO;
import co.istad.ishop.repository.RoleRepository;
import co.istad.ishop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Role createRole(CreateRoleDTO role) throws RoleException {
        String normalizedName = role.name().toUpperCase(Locale.ROOT);
        Role findRole = roleRepository.findRoleByName(normalizedName);
        if(findRole != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT ,"ROLE_ALREADY_EXISTS");
        }
        Role createRole = roleMapper.toRole(role);
        createRole.setUuid(UUID.randomUUID().toString());
        createRole.setName(role.name().toUpperCase(Locale.ROOT));
        return roleRepository.save(createRole);
    }

    @Override
    public List<RoleRespondDTO> getRoles() {
        List<RoleRespondDTO> roleRespondDTOS = roleRepository.findAll().stream()
                .map(roleMapper::toRoleRespondDTO)
                .toList();
        if (roleRespondDTOS.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Role not found");
        }
        return roleRespondDTOS;
    }
}
