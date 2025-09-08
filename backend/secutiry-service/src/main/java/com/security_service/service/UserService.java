package com.security_service.service;

import com.security_service.dto.CreateUserRequest;
import com.security_service.dto.UserResponse;
import com.security_service.entity.Role;
import com.security_service.entity.User;
import com.security_service.exception.ResourceAlreadyExistsException;
import com.security_service.exception.ResourceNotFoundException;
import com.security_service.mapper.UserMapper;
import com.security_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    private final UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(String.valueOf(user.getRole()))
                .build();

    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        return mapper.toResponse(repository.save(mapper.toEntity(request, Role.USER)));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }

        repository.deleteById(id);
    }

    public List<UserResponse> getAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public UserResponse getById(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id))
        );
    }
}
