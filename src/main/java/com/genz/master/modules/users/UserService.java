package com.genz.master.modules.users;

import java.util.List;
import java.util.Optional;

import com.genz.master.modules.users.dto.UserRequestDto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.listAll();
    }

    @Transactional
    public UserEntity create(UserRequestDto userDto) {
        UserEntity user = new UserEntity();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        userRepository.persist(user);
        return user;
    }

    public Optional<UserEntity> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Transactional
    public Optional<UserEntity> update(Long id, UserRequestDto userDto) {
        Optional<UserEntity> existingUserOptional = userRepository.findByIdOptional(id);
        if (existingUserOptional.isPresent()) {
            UserEntity existingUser = existingUserOptional.get();
            existingUser.setName(userDto.getName());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setPassword(userDto.getPassword());

            userRepository.persist(existingUser);
            return Optional.of(existingUser); // Kembalikan user yang telah diperbarui
        }

        return Optional.empty(); // Jika user tidak ditemukan
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.deleteById(id);
    }
}
