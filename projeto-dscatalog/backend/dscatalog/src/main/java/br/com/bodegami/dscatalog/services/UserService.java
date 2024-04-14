package br.com.bodegami.dscatalog.services;

import br.com.bodegami.dscatalog.dto.RoleDTO;
import br.com.bodegami.dscatalog.dto.UserDTO;
import br.com.bodegami.dscatalog.dto.UserInsertDTO;
import br.com.bodegami.dscatalog.dto.UserUpdateDTO;
import br.com.bodegami.dscatalog.entities.Role;
import br.com.bodegami.dscatalog.entities.User;
import br.com.bodegami.dscatalog.projections.UserDetailsProjection;
import br.com.bodegami.dscatalog.repositories.RoleRepository;
import br.com.bodegami.dscatalog.repositories.UserRepository;
import br.com.bodegami.dscatalog.services.exceptions.DatabaseException;
import br.com.bodegami.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource not found: %d", id)));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        User response = userRepository.save(entity);
        return new UserDTO(response);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User entity = userRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            User response = userRepository.save(entity);
            return new UserDTO(response);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Id not found: %d", id));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for (RoleDTO roleDTO: dto.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
        if (result.size() == 0) {
            throw new UsernameNotFoundException("Email not found");
        }

        User user = new User(); 
        user.setEmail(result.get(0).getUsername());
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }
}
