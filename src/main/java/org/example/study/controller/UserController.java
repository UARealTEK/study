package org.example.study.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.study.DTOs.Gender;
import org.example.study.DTOs.UserDto;
import org.example.study.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 2️⃣ Create custom exceptions
 * 3️⃣ Add pagination & filtering
 * 4️⃣ Write controller tests
 */

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return service.getAllUsers();
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
        System.out.println("controller HIT");
        UserDto dto = service.updateUser(body,id);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull Long id) {
        service.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(params = {"age", "gender"})
    public List<UserDto> findUsersByAgeAndGender(@RequestParam Integer age, @RequestParam Gender gender) {
        return service.findUserByAgeAndGender(age,gender);
    }
}
