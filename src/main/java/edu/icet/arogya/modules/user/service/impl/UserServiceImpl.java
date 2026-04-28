package edu.icet.arogya.modules.user.service.impl;

import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.modules.user.dto.UserResponse;
import edu.icet.arogya.modules.user.entity.User;
import edu.icet.arogya.modules.user.mapper.UserMapper;
import edu.icet.arogya.modules.user.repository.UserRepository;
import edu.icet.arogya.modules.user.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.mapToResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.mapToResponse(user);
    }

    @Override
    public Page<@NonNull UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::mapToResponse);
    }
}
