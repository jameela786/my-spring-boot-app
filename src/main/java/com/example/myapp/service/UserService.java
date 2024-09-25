package com.example.myapp.service;

import com.example.myapp.model.User;
import com.example.myapp.repository.UserRepository;
import com.example.myapp.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;

    @Autowired
    public UserService(UserRepository userRepository, ManagerRepository managerRepository) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
    }

    public User createUser(User user) {
        user.setManagerRepository(managerRepository);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUserById(Long userId) {
        return userRepository.findById(userId).map(List::of).orElse(List.of());
    }

    public List<User> getUserByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber).map(List::of).orElse(List.of());
    }

    public List<User> getUsersByManagerId(Long managerId) {
        return userRepository.findByManagerId(managerId);
    }


    // public void deleteUser(Long userId) {
    //     // Delete user record from the database
    //     userRepository.deleteById(userId);
    // }

    public void deleteUserById(Long userId) {
      Optional<User> optionalUser = userRepository.findById(userId);
      if (optionalUser.isPresent()) {
          userRepository.deleteById(userId);
      } else {
          throw new IllegalArgumentException("User not found with user_id: " + userId);
      }
    }

    public void deleteUserByMobileNumber(String mobileNumber) {
        Optional<User> optionalUser = userRepository.findByMobileNumber(mobileNumber);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        } else {
            throw new IllegalArgumentException("User not found with mobile number: " + mobileNumber);
        }
    }

    public String bulkUpdateUsers(List<Long> userIds, Map<String, Object> updateData) {
        List<User> usersToUpdate = userRepository.findAllById(userIds);

        // Check if all users exist
        if (usersToUpdate.size() != userIds.size()) {
            return "Some user_ids do not exist.";
        }

        Long managerId = (Long) updateData.get("manager_id");

        // Validate manager exists if manager_id is being updated
        if (managerId != null) {
            if (!managerRepository.existsById(managerId)) {
                return "The manager with manager_id " + managerId + " does not exist.";
            }
        }

        // Update manager_id for all users
        for (User user : usersToUpdate) {
            if (managerId != null) {
                if (user.getManagerId() != null) {
                    // Set current entry to inactive
                    user.setActive(false);
                    userRepository.save(user);

                    // Create a new entry for the user with the new manager_id and updated_at
                    User newUser = new User();
                    newUser.setFullName(user.getFullName());
                    newUser.setMobileNumber(user.getMobileNumber());
                    newUser.setPanNumber(user.getPanNumber());
                    newUser.setManagerId(managerId);
//                    newUser.setCreatedAt(user.getCreatedAt());
                    newUser.setActive(true);
                    userRepository.save(newUser);
                } else {
                    // Set the manager_id for the first time
                    user.setManagerId(managerId);
//                    user.setUpdatedAt(LocalDateTime.now());
                    userRepository.save(user);
                }
            }
        }

        return "Users updated successfully.";
    }

    public String updateUser(Long userId, Map<String, Object> updateData) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (updateData.containsKey("full_name")) {
                user.setFullName((String) updateData.get("full_name"));
            }

            if (updateData.containsKey("mob_num")) {
                String mobileNumber = (String) updateData.get("mob_num");
                if (userRepository.findByMobileNumber(mobileNumber).isPresent()) {
                    return "Mobile number already exists: " + mobileNumber;
                }
                user.setMobileNumber(mobileNumber);
            }

            if (updateData.containsKey("pan_num")) {
                user.setPanNumber((String) updateData.get("pan_num"));
            }

            if (updateData.containsKey("manager_id")) {
                Long managerId = (Long) updateData.get("manager_id");
                if (!managerRepository.existsById(managerId)) {
                    return "The manager with manager_id " + managerId + " does not exist.";
                }
                user.setManagerId(managerId);
            }

//            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            return "User updated successfully.";
        } else {
            return "User not found with ID: " + userId;
        }
    }
}
