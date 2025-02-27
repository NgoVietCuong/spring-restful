package com.nvc.spring_boot.service;

import com.nvc.spring_boot.domain.dto.*;
import com.nvc.spring_boot.util.error.BadRequestException;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import com.nvc.spring_boot.domain.User;
import com.nvc.spring_boot.repository.UserRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PaginationDTO getList(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(specification, pageable);
        PaginationDTO result = new PaginationDTO();
        MetaDTO meta = new MetaDTO();

        meta.setCurrentPage(pageable.getPageNumber() + 1);
        meta.setItemsPerPage(pageable.getPageSize());
        meta.setTotalPages(pageUser.getTotalPages());
        meta.setTotalItems(pageUser.getTotalElements());

        List<ResUserDTO> resUsers = pageUser.getContent().stream().map(item -> {
            ResUserDTO resUser = new ResUserDTO();
            BeanUtils.copyProperties(item, resUser);
            return resUser;
        }).collect(Collectors.toList());
        result.setMeta(meta);
        result.setContent(resUsers);

        return result;
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public ResUpdateUserDTO updateUser(User userData) throws ResourceNotFoundException {
        User user = findUser(userData.getId());
        if (user != null) {
            user.setName(userData.getName());
            user.setGender(userData.getGender());
            user.setAddress(userData.getAddress());

            User updatedUser = userRepository.save(user);
            ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();
            BeanUtils.copyProperties(updatedUser, resUpdateUserDTO);

            return resUpdateUserDTO;
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public ResCreateUserDTO createUser(User userData) throws BadRequestException {
        Boolean isUserExists = userRepository.existsByEmail(userData.getEmail());
        if (isUserExists) {
            throw new BadRequestException("Email already exists");
        }

        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        User newUser = userRepository.save(userData);

        ResCreateUserDTO resCreateUser = new ResCreateUserDTO();
        BeanUtils.copyProperties(newUser, resCreateUser);
        return resCreateUser;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUserToken(User user, String token) {
        user.setRefreshToken(token);
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByEmailAndRefreshToken(String email, String token) {
        return userRepository.findByEmailAndRefreshToken(email, token);
    }
}
