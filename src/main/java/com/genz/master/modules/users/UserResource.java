package com.genz.master.modules.users;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.genz.master.common.ApiResponse;
import com.genz.master.modules.users.dto.UserRequestDto;
import com.genz.master.modules.users.dto.UserResponseDto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RequiredArgsConstructor
public class UserResource {

    private final UserService service;

    @GET
    public ApiResponse<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = service.getAllUsers().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
        return ApiResponse.ok(users, "Users retrieved successfully");
    }

    @POST
    public ApiResponse<UserResponseDto> create(@Valid UserRequestDto userDto) {
        try {
            UserEntity createdUserEntity = service.create(userDto);
            UserResponseDto createdUser = new UserResponseDto(createdUserEntity);
            return ApiResponse.ok(createdUser, "User created successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to create user: " + e.getMessage());
        }
    }

    @GET
    @Path("/{name}")
    public ApiResponse<UserResponseDto> getUserByName(@PathParam("name") String name) {
        return service.getUserByName(name)
                .map(user -> ApiResponse.ok(new UserResponseDto(user), "User found"))
                .orElse(ApiResponse.notFound("User not found"));
    }

    @PUT
    @Path("/{id}")
    public ApiResponse<UserResponseDto> update(@PathParam("id") Long id, @Valid UserRequestDto userDto) {
        try {
            Optional<UserEntity> updatedUserOptional = service.update(id, userDto);
            if (updatedUserOptional.isPresent()) {
                UserResponseDto updatedUser = new UserResponseDto(updatedUserOptional.get()); // Convert ke DTO
                return ApiResponse.ok(updatedUser, "User updated successfully");
            } else {
                return ApiResponse.notFound("User not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to update user: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        try {
            boolean deleted = service.delete(id);
            if (deleted) {
                return ApiResponse.ok(null, "User deleted successfully");
            } else {
                return ApiResponse.notFound("User not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete user: " + e.getMessage());
        }
    }
}
