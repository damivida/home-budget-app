package com.example.homebudgetapp.user.controller;

import com.example.homebudgetapp.core.configuration.openapi.OpenApiTags;
import com.example.homebudgetapp.user.dto.*;
import com.example.homebudgetapp.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home-budget-app/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(tags = OpenApiTags.USER, summary = "Register user.")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserDto user) throws Exception {
        return new ResponseEntity<>(userService.registerNewUserAccount(user), HttpStatus.OK);
    }

    @PostMapping("/income")
    @Operation(tags = OpenApiTags.USER, summary = "Add user income.")
    public ResponseEntity<UserIncomeResponse> addUserIncome(@Valid  @RequestBody UserIncomeDto userIncomeDto) throws Exception {
        return new ResponseEntity<>(userService.addUserIncome(userIncomeDto), HttpStatus.OK);
    }

    @PutMapping("/data")
    @Operation(tags = OpenApiTags.USER, summary = "Update user data.")
    public ResponseEntity<UserResponse> updateUser(final @PathVariable("id") Long id, @RequestBody UserDto userDto) throws Exception {
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }

    @GetMapping("/data")
    @Operation(tags = OpenApiTags.USER, summary = "Get user data.")
    public ResponseEntity<UserResponse> getUserData() {
        return new ResponseEntity<>(userService.getUserData(), HttpStatus.OK);
    }

    @GetMapping("/income")
    @Operation(tags = OpenApiTags.USER, summary = "Get users income.")
    public ResponseEntity<List<UserIncomeResponse>> getUserIncome() {
        return new ResponseEntity<>(userService.getUserIncome(), HttpStatus.OK);
    }
}
