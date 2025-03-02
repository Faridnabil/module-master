package com.genz.master.modules.users;

import com.genz.master.modules.users.dto.UserRequestDto;
import com.genz.master.utility.PasswordHasher;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JsonWebToken jwt;

    public List<UserEntity> getAllUsers() {
        if (!jwt.getGroups().contains("Admin")) {
            return userRepository.find("username", jwt.getName()).list();
        }
        return userRepository.listAll();
    }

    @Transactional
    public UserEntity create(UserRequestDto userDto) {
        UserEntity user = new UserEntity();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());

        String hashedPassword = PasswordHasher.hash(userDto.getPassword());
        user.setPassword(hashedPassword);

        user.setRole(userDto.getRole());

        userRepository.persist(user);
        return user;
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public Optional<UserEntity> update(Long id, UserRequestDto userDto) {
        Optional<UserEntity> existingUserOptional = userRepository.findByIdOptional(id);
        if (existingUserOptional.isPresent()) {
            UserEntity existingUser = existingUserOptional.get();
            existingUser.setUsername(userDto.getUsername());
            existingUser.setEmail(userDto.getEmail());

            // Hash password baru jika diubah
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                String hashedPassword = PasswordHasher.hash(userDto.getPassword());
                existingUser.setPassword(hashedPassword);
            }

            userRepository.persist(existingUser);
            return Optional.of(existingUser);
        }
        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.deleteById(id);
    }
}