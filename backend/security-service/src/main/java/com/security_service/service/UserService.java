package com.security_service.service;

import com.security_service.dto.RegisterRequest;
import com.security_service.dto.UserResponse;
import com.security_service.entity.Role;
import com.security_service.entity.User;
import com.security_service.exception.ResourceAlreadyExistsException;
import com.security_service.exception.ResourceNotFoundException;
import com.security_service.mapper.UserMapper;
import com.security_service.repository.UserRepository;
import com.security_service.util.PasswordEncodingStrategy;
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

    private final PasswordEncodingStrategy strategy;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().toString())
                .build();

    }

    @Transactional
    public UserResponse create(RegisterRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = mapper.toEntity(request, Role.USER);
        user.setPassword(strategy.encode(user.getPassword()));

        return mapper.toResponse(repository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id" + id);
        }

        repository.deleteById(id);
    }

    public List<UserResponse> getAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public UserResponse getById(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id=" + id))
        );
    }

    public UserResponse getByEmail(String email) {
        return mapper.toResponse(repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email=" + email))
        );
    }
}
