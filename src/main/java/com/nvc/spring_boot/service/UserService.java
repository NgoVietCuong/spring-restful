package com.nvc.spring_boot.service;

import com.nvc.spring_boot.dto.user.request.CreateUserRequest;
import com.nvc.spring_boot.dto.user.request.UpdateUserRequest;
import com.nvc.spring_boot.dto.user.response.BaseUserResponse;
import com.nvc.spring_boot.entity.Company;
import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.dto.user.response.CreateUserResponse;
import com.nvc.spring_boot.dto.user.response.UpdateUserResponse;
import com.nvc.spring_boot.dto.user.response.FindUserResponse;
import com.nvc.spring_boot.entity.Role;
import com.nvc.spring_boot.repository.CompanyRepository;
import com.nvc.spring_boot.repository.RoleRepository;
import com.nvc.spring_boot.util.error.BadRequestException;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import com.nvc.spring_boot.entity.User;
import com.nvc.spring_boot.repository.UserRepository;

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
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyRepository companyRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public PaginationResponse getList(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(specification, pageable);
        PaginationResponse.PaginationMeta meta = PaginationResponse.PaginationMeta.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .itemsPerPage(pageable.getPageSize())
                .totalPages(pageUser.getTotalPages())
                .totalItems(pageUser.getTotalElements())
                .build();

        List<FindUserResponse> resUsers = pageUser.getContent().stream().map(user -> {
            FindUserResponse.UserCompany userCompany = convertToUserCompany(user.getCompany());
            FindUserResponse.UserRole userRole = convertToUserRole(user.getRole());

            return FindUserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .address(user.getAddress())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .company(userCompany)
                    .role(userRole)
                    .build();
        }).collect(Collectors.toList());

        return PaginationResponse.builder()
                .meta(meta)
                .content(resUsers)
                .build();
    }

    public FindUserResponse findUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            FindUserResponse.UserCompany userCompany = convertToUserCompany(user.getCompany());
            FindUserResponse.UserRole userRole = convertToUserRole(user.getRole());

            return FindUserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .address(user.getAddress())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .company(userCompany)
                    .role(userRole)
                    .build();
        }

        return null;
    }

    public UpdateUserResponse updateUser(UpdateUserRequest userData) throws ResourceNotFoundException {
        User user = userRepository.findById(userData.getId()).orElse(null);
        if (user != null) {
            user.setName(userData.getName());
            user.setGender(userData.getGender());
            user.setAddress(userData.getAddress());

            Company company = companyRepository.findById(userData.getCompany().getId()).orElse(null);
            user.setCompany(company);

            Role role = roleRepository.findById(userData.getRole().getId()).orElse(null);
            user.setRole(role);

            userRepository.save(user);

            UpdateUserResponse.UserCompany userCompany = convertToUserCompany(user.getCompany());
            UpdateUserResponse.UserRole userRole = convertToUserRole(user.getRole());

            return UpdateUserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .address(user.getAddress())
                    .updatedAt(user.getUpdatedAt())
                    .company(userCompany)
                    .role(userRole)
                    .build();
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public CreateUserResponse createUser(CreateUserRequest userData) throws BadRequestException {
        Boolean isUserExists = userRepository.existsByEmail(userData.getEmail());
        if (isUserExists) {
            throw new BadRequestException("Email already exists");
        }

        Company company = companyRepository.findById(userData.getCompany().getId()).orElse(null);
        userData.setCompany(company);

        Role role = roleRepository.findById(userData.getRole().getId()).orElse(null);
        userData.setRole(role);

        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        User newUser = User.builder()
                .name(userData.getName())
                .email(userData.getEmail())
                .password(userData.getPassword())
                .gender(userData.getGender())
                .address(userData.getAddress())
                .company(userData.getCompany())
                .role(userData.getRole())
                .build();
        userRepository.save(newUser);

        CreateUserResponse.UserCompany userCompany = convertToUserCompany(newUser.getCompany());
        CreateUserResponse.UserRole userRole = convertToUserRole(newUser.getRole());

        return CreateUserResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .gender(newUser.getGender())
                .address(newUser.getAddress())
                .createdAt(newUser.getCreatedAt())
                .company(userCompany)
                .role(userRole)
                .build();
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

    private BaseUserResponse.UserCompany convertToUserCompany(Company company) {
        if (company == null) return null;
        return BaseUserResponse.UserCompany.builder()
                .id(company.getId())
                .name(company.getName())
                .build();
    }

    private BaseUserResponse.UserRole convertToUserRole(Role role) {
        if (role == null) return null;
        return BaseUserResponse.UserRole.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
