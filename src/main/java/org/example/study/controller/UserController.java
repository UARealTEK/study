package org.example.study.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserPatchDto;
import org.example.study.enums.Gender;
import org.example.study.DTOs.UserDto;
import org.example.study.service.BorrowService;
import org.example.study.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Also Add:
 * - ReadME
 * - Integration tests ? (Testcontainers)
 * - Database migrations (?)
 */

@SuppressWarnings("unused")

//TODO:
// - Get All books borrowed by user:
// GET /users/{id}/borrows
@Validated
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final BorrowService borrowService;

    @GetMapping(value = {"/", ""})
    public PageResponseDTO<UserDto> searchUsers(@PageableDefault(size = 5, sort = "age") Pageable page,
                                                @RequestParam(required = false) Integer age,
                                                @RequestParam(required = false) String fullName,
                                                @RequestParam(required = false) Gender gender) {
        return userService.getAllUsers(page, age,fullName,gender);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/{id}/borrows")
    public PageResponseDTO<BorrowRecordResponseDto> getBorrowsByUser(Pageable pageable, @PathVariable Long userId) {
        return borrowService.getBorrowedBooksRecordsByUser(pageable, userId);
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.saveUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto body, @PathVariable @NotNull Long id) {
        UserDto dto = userService.updateUser(body,id);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> patchUser(@Valid @RequestBody UserPatchDto body, @PathVariable @NotNull Long id) {
        UserDto dto = userService.patchUser(body, id);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
