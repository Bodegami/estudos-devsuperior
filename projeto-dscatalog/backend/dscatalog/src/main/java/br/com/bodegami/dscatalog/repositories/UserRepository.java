package br.com.bodegami.dscatalog.repositories;

import br.com.bodegami.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long > {
}
