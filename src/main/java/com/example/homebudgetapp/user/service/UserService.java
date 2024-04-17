package com.example.homebudgetapp.user.service;

import com.example.homebudgetapp.core.configuration.security.SecurityUtils;
import com.example.homebudgetapp.core.exception.HomeBudgetException;
import com.example.homebudgetapp.core.exception.Messages;
import com.example.homebudgetapp.user.dto.*;
import com.example.homebudgetapp.user.entity.User;
import com.example.homebudgetapp.user.entity.UserIncome;
import com.example.homebudgetapp.user.repository.UserIncomeRepository;
import com.example.homebudgetapp.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final AuthenticationManager authenticationManager;
     private final UserIncomeRepository userIncomeRepository;

    public UserResponse registerNewUserAccount(UserDto userDto) throws Exception {

        Optional<User> byUsername = userRepository.findByUsername(userDto.getUsername());

        if (byUsername.isPresent()) {
            throw new HomeBudgetException(Messages.ENTITY_EXISTS);
        }

        User user = toEntityUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = saveAndFlushUser(user);
        return fromEntityUser(user);
    }


    public LoginResponse loginUser(LoginDto loginDto) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(loginDto.getUsername());
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                            loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authenticate);
            loginResponse.setHttpStatus(HttpStatus.OK);
            loginResponse.setMessage("Login successful!");
        }catch (Exception e) {
            loginResponse.setMessage("Login failed: " + e.getMessage());
            loginResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        }
        return loginResponse;
    }

    public User saveAndFlushUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public UserResponse getUserData() {
        UserDetails loggedInUser = SecurityUtils.getLoggedInUser();
        User user = findUserByUsername(loggedInUser.getUsername());
        return fromEntityUser(user);
    }


    public UserResponse updateUser(UserDto userDto) {

        UserDetails loggedInUser = SecurityUtils.getLoggedInUser();
        User user = findUserByUsername(loggedInUser.getUsername());

        if (Objects.nonNull(userDto.getUsername())) {
            user.setUsername(userDto.getUsername());
        }

        if (Objects.nonNull(userDto.getEmail())) {
            user.setEmail(userDto.getEmail());
        }

        if (Objects.nonNull(userDto.getPassword())) {
            user.setPassword(userDto.getPassword());
        }

        userRepository.saveAndFlush(user);
        return fromEntityUser(user);
    }


    @Transactional
    public UserIncomeResponse addUserIncome(UserIncomeDto userIncomeDto) {

        UserDetails principal = SecurityUtils.getLoggedInUser();
        User user = findUserByUsername(principal.getUsername());

        user.setBalance(user.getBalance().add(userIncomeDto.getAmount()));
        userRepository.saveAndFlush(user);

        UserIncome userIncome = toEntityUserIncome(userIncomeDto, user);

        userIncomeRepository.saveAndFlush(userIncome);
        return fromEntityUserIncome(userIncome);

    }

    public List<UserIncomeResponse> getUserIncome() {
        List<UserIncome> userIncomeList = userIncomeRepository.findAll();
        return userIncomeList.stream().map(this::fromEntityUserIncome).toList();
    }

   public BigDecimal findTotalIncomeBetweenDates(LocalDateTime fromDate, LocalDateTime toDate) {
        return userIncomeRepository.findTotalIncomeBetweenDates(fromDate, toDate);
    }


    private User toEntityUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    private UserResponse fromEntityUser(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setBalance(user.getBalance());
        return userResponse;
    }


    private UserIncome toEntityUserIncome(UserIncomeDto userIncomeDto, User user) {

        UserIncome userIncome = new UserIncome();
        userIncome.setIncomeType(userIncomeDto.getIncomeType());
        userIncome.setDescription(userIncomeDto.getDescription());
        userIncome.setAmount(userIncomeDto.getAmount());
        userIncome.setUser(user);

        return userIncome;
    }

    private UserIncomeResponse fromEntityUserIncome(UserIncome userIncome) {
        UserIncomeResponse userIncomeResponse = new UserIncomeResponse();
        userIncomeResponse.setIncomeType(userIncome.getIncomeType());
        userIncomeResponse.setDescription(userIncome.getDescription());
        userIncomeResponse.setAmount(userIncome.getAmount());
        userIncomeResponse.setUsername(userIncome.getUser().getUsername());
        userIncomeResponse.setCreatedAt(userIncome.getCreatedAt());
        return userIncomeResponse;
    }
}
