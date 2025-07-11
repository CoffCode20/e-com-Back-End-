package co.istad.ishop.service;

import co.istad.ishop.entities.Role;
import co.istad.ishop.exception.RoleException;
import co.istad.ishop.model.request.CreateRoleDTO;
import co.istad.ishop.model.response.RoleRespondDTO;

import java.util.List;

public interface RoleService {

    Role createRole(CreateRoleDTO role) throws RoleException;

    List<RoleRespondDTO> getRoles();
}
