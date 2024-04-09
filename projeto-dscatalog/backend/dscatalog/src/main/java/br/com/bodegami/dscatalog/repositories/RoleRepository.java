package br.com.bodegami.dscatalog.repositories;

import br.com.bodegami.dscatalog.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
