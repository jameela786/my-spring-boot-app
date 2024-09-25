package com.example.myapp.controller;

import com.example.myapp.model.User;
// import com.example.myapp.repository.UserRepository;
import com.example.myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create_user")
    public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/get_users")
    public ResponseEntity<Map<String, List<User>>> getUsers(@RequestBody Map<String, String> filters) {
        String userId = filters.get("user_id");
        String mobileNumber = filters.get("mob_num");
        String managerId = filters.get("manager_id");

        List<User> users;

        // Apply filters based on input
        if (userId != null) {
            users = userService.getUserById(Long.parseLong(userId));
        } else if (mobileNumber != null) {
            users = userService.getUserByMobileNumber(mobileNumber);
        } else if (managerId != null) {
            users = userService.getUsersByManagerId(Long.parseLong(managerId));
        } else {
            users = userService.getAllUsers();
        }

        return new ResponseEntity<>(Map.of("users", users), HttpStatus.OK);
    }

    // @DeleteMapping("/delete_user/{userId}")
    // public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
    //     userService.deleteUser(userId);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }

    @PostMapping("/delete_user")
    public ResponseEntity<String> deleteUser(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("user_id");
        String mobileNumber = requestBody.get("mob_num");

        if (userId != null) {
            userService.deleteUserById(Long.parseLong(userId));
        } else if (mobileNumber != null) {
            userService.deleteUserByMobileNumber(mobileNumber);
        } else {
            return new ResponseEntity<>("Either user_id or mob_num must be provided.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User successfully deleted.", HttpStatus.OK);
    }


    @PostMapping ("/update_user")
    public ResponseEntity<String> updateUser(@RequestBody Map<String, Object> requestBody) {
//        List<Long> userIds = (List<Long>) requestBody.get("user_ids");
//        List<Long> userIds = ((List<?>) requestBody.get("user_ids")).stream().map(Integer::longValue).collect(Collectors.toList())
        List<Long> userIds = ((List<?>) requestBody.get("user_ids")).stream()
                .map(id -> {
                    if (id instanceof Integer) {
                        return ((Integer) id).longValue(); // Convert Integer to Long
                    } else if (id instanceof Long) {
                        return (Long) id; // Keep Long as is
                    } else {
                        throw new IllegalArgumentException("Invalid type for user ID: " + id.getClass().getName());
                    }
                })
                .collect(Collectors.toList());

        Map<String, Object> updateData = (Map<String, Object>) requestBody.get("update_data");

        if (userIds == null || userIds.isEmpty()) {
            return new ResponseEntity<>("user_ids must be provided.", HttpStatus.BAD_REQUEST);
        }

        if (updateData == null || updateData.isEmpty()) {
            return new ResponseEntity<>("update_data must be provided.", HttpStatus.BAD_REQUEST);
        }

        // Bulk update logic
        if (userIds.size() > 1 && updateData.containsKey("manager_id")) {
            String result = userService.bulkUpdateUsers(userIds, updateData);
            if (result.equals("Users updated successfully.")) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        }

        // Individual update logic
        if (userIds.size() == 1) {
            String result = userService.updateUser(userIds.get(0), updateData);
            if (result.equals("User updated successfully.")) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("Bulk update only allowed for manager_id.", HttpStatus.BAD_REQUEST);
    }
}
