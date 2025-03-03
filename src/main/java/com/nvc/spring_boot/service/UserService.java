package com.nvc.spring_boot.service;

import com.nvc.spring_boot.domain.Company;
import com.nvc.spring_boot.domain.response.PaginationDTO;
import com.nvc.spring_boot.domain.response.ResCreateUserDTO;
import com.nvc.spring_boot.domain.response.ResUpdateUserDTO;
import com.nvc.spring_boot.domain.response.ResUserDTO;
import com.nvc.spring_boot.repository.CompanyRepository;
import com.nvc.spring_boot.util.error.BadRequestException;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import com.nvc.spring_boot.domain.User;
import com.nvc.spring_boot.repository.UserRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
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

    public PaginationDTO getList(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(specification, pageable);
        PaginationDTO result = new PaginationDTO();
        PaginationDTO.MetaDTO meta = new PaginationDTO.MetaDTO();

        meta.setCurrentPage(pageable.getPageNumber() + 1);
        meta.setItemsPerPage(pageable.getPageSize());
        meta.setTotalPages(pageUser.getTotalPages());
        meta.setTotalItems(pageUser.getTotalElements());

        List<ResUserDTO> resUsers = pageUser.getContent().stream().map(item -> {
            ResUserDTO resUser = new ResUserDTO();
            BeanUtils.copyProperties(item, resUser);

            ResUserDTO.UserCompany userCompany = new ResUserDTO.UserCompany();
            resUser.setCompany(item.getCompany() != null ? userCompany : null);

            if (item.getCompany() != null) {
                BeanUtils.copyProperties(item.getCompany(), resUser.getCompany());
            }

            return resUser;
        }).collect(Collectors.toList());
        result.setMeta(meta);
        result.setContent(resUsers);

        return result;
    }

    public ResUserDTO findUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            ResUserDTO resUser = new ResUserDTO();
            BeanUtils.copyProperties(user, resUser);

            ResUserDTO.UserCompany userCompany = new ResUserDTO.UserCompany();
            resUser.setCompany(user.getCompany() != null ? userCompany : null);

            if (user.getCompany() != null) {
                BeanUtils.copyProperties(user.getCompany(), resUser.getCompany());
            }

            return resUser;
        }

        return null;
    }

    public ResUpdateUserDTO updateUser(User userData) throws ResourceNotFoundException {
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

            ResUpdateUserDTO resUpdateUser = new ResUpdateUserDTO();
            BeanUtils.copyProperties(updatedUser, resUpdateUser);

            ResUpdateUserDTO.UserCompany userCompany = new ResUpdateUserDTO.UserCompany();
            resUpdateUser.setCompany(updatedUser.getCompany() != null ? userCompany : null);

            if (updatedUser.getCompany() != null) {
                BeanUtils.copyProperties(updatedUser.getCompany(), resUpdateUser.getCompany());
            }

            return resUpdateUser;
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public ResCreateUserDTO createUser(User userData) throws BadRequestException {
        Boolean isUserExists = userRepository.existsByEmail(userData.getEmail());
        if (isUserExists) {
            throw new BadRequestException("Email already exists");
        }

        if (userData.getCompany() != null) {
            Company company = companyRepository.findById(userData.getCompany().getId()).orElse(null);
            userData.setCompany(company);
        }

        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        User newUser = userRepository.save(userData);

        ResCreateUserDTO resCreateUser = new ResCreateUserDTO();
        BeanUtils.copyProperties(newUser, resCreateUser);

        ResCreateUserDTO.UserCompany userCompany = new ResCreateUserDTO.UserCompany();
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
