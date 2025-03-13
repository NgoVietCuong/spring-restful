package com.nvc.spring_boot.service;

import com.nvc.spring_boot.dto.user.request.CreateUserRequest;
import com.nvc.spring_boot.dto.user.request.UpdateUserRequest;
import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.dto.user.response.CreateUserResponse;
import com.nvc.spring_boot.dto.user.response.UpdateUserResponse;
import com.nvc.spring_boot.dto.user.response.FindUserResponse;
import com.nvc.spring_boot.repository.CompanyRepository;
import com.nvc.spring_boot.util.error.BadRequestException;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import com.nvc.spring_boot.entity.User;
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
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PaginationResponse getList(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(specification, pageable);
        PaginationResponse.PaginationMeta meta = PaginationResponse.PaginationMeta.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .itemsPerPage(pageable.getPageSize())
                .totalPages(pageUser.getTotalPages())
                .totalItems(pageUser.getTotalElements())
                .build();

        List<FindUserResponse> resUsers = pageUser.getContent().stream().map(item -> {
            FindUserResponse resUser = new FindUserResponse();
            BeanUtils.copyProperties(item, resUser);

            FindUserResponse.UserCompany userCompany = new FindUserResponse.UserCompany();
            resUser.setCompany(item.getCompany() != null ? userCompany : null);

            if (item.getCompany() != null) {
                BeanUtils.copyProperties(item.getCompany(), resUser.getCompany());
            }

            return resUser;
        }).collect(Collectors.toList());

        return PaginationResponse.builder()
                .meta(meta)
                .content(resUsers)
                .build();
    }

    public FindUserResponse findUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            FindUserResponse resUser = new FindUserResponse();
            BeanUtils.copyProperties(user, resUser);

            FindUserResponse.UserCompany userCompany = new FindUserResponse.UserCompany();
            resUser.setCompany(user.getCompany() != null ? userCompany : null);

            if (user.getCompany() != null) {
                BeanUtils.copyProperties(user.getCompany(), resUser.getCompany());
            }

            return resUser;
        }

        return null;
    }

    public UpdateUserResponse updateUser(UpdateUserRequest userData) throws ResourceNotFoundException {
        User user = userRepository.findById(userData.getId()).orElse(null);
        if (user != null) {
            user.setName(userData.getName());
            user.setGender(userData.getGender());
            user.setAddress(userData.getAddress());

            if (userData.getCompany() != null) {
                Company company = companyRepository.findById(userData.getCompany().getId()).orElse(null);
                user.setCompany(company);
            }
            User updatedUser = userRepository.save(user);

            UpdateUserResponse resUpdateUser = new UpdateUserResponse();
            BeanUtils.copyProperties(updatedUser, resUpdateUser);

            UpdateUserResponse.UserCompany userCompany = new UpdateUserResponse.UserCompany();
            resUpdateUser.setCompany(updatedUser.getCompany() != null ? userCompany : null);

            if (updatedUser.getCompany() != null) {
                BeanUtils.copyProperties(updatedUser.getCompany(), resUpdateUser.getCompany());
            }

            return resUpdateUser;
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public CreateUserResponse createUser(CreateUserRequest userData) throws BadRequestException {
        Boolean isUserExists = userRepository.existsByEmail(userData.getEmail());
        if (isUserExists) {
            throw new BadRequestException("Email already exists");
        }

        if (userData.getCompany() != null) {
            Company company = companyRepository.findById(userData.getCompany().getId()).orElse(null);
            userData.setCompany(company);
        }

        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        User newUser = User.builder()
                .name(userData.getName())
                .email(userData.getEmail())
                .password(userData.getPassword())
                .gender(userData.getGender())
                .address(userData.getAddress())
                .company(userData.getCompany())
                .build();
        userRepository.save(newUser);

        CreateUserResponse resCreateUser = new CreateUserResponse();
        BeanUtils.copyProperties(newUser, resCreateUser);

        CreateUserResponse.UserCompany userCompany = new CreateUserResponse.UserCompany();
        resCreateUser.setCompany(newUser.getCompany() != null ? userCompany : null);

        if (newUser.getCompany() != null) {
            BeanUtils.copyProperties(newUser.getCompany(), resCreateUser.getCompany());
        }

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
