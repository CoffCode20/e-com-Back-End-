package co.istad.ishop.repository;

import co.istad.ishop.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findRoleByUuid(String uuid);

    Role findRoleByName(String name);
}
