package org.example.study.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.enums.Gender;
import org.example.study.DTOs.UserDto;
import org.example.study.service.UserService;
import org.example.study.util.Converters.Converter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 3️⃣ Add pagination & filtering
 * Also Add:
 * - PATCH request (PATCH /id)
 * - Look into Global Exception handling (RestControllerAdvice)
 * - API Response wrapper
 * - Sorting support + dynamic filtering (Specifications)
 * - Swagger
 * LATER:
 * - ReadME
 * - Caching (@Cachable)
 * - Security
 * - Integration tests ? (Testcontainers)
 * - Database migrations (?)
 */

@SuppressWarnings("unused")
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponseDTO<UserDto> getAllUsers(@PageableDefault(size = 5, sort = "age") Pageable page) {
        return Converter.toPageObj(service.getAllUsers(page));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return service.getUserByID(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = service.saveUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto body, @PathVariable @NotNull Long id) {
        UserDto dto = service.updateUser(body,id);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull Long id) {
        service.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(params = {"age", "gender"})
    public PageResponseDTO<UserDto> findUsersByAgeAndGender(@PageableDefault(size = 5, sort = "age") Pageable pageable,
                                                            @RequestParam Integer age,
                                                            @RequestParam Gender gender) {
        return Converter.toPageObj(service.findUserByAgeAndGender(pageable, age, gender));
    }
}
