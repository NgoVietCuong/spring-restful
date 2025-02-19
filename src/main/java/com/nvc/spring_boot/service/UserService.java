package com.nvc.spring_boot.service;

import com.nvc.spring_boot.util.error.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nvc.spring_boot.domain.dto.MetaDTO;
import com.nvc.spring_boot.domain.dto.PaginationDTO;
import com.nvc.spring_boot.domain.dto.ResCreateUserDTO;
import com.nvc.spring_boot.domain.dto.ResUpdateUserDTO;
import com.nvc.spring_boot.domain.User;
import com.nvc.spring_boot.repository.UserRepository;

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

        result.setMeta(meta);
        result.setContent(pageUser.getContent());

        return result;
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public ResUpdateUserDTO updateUser(User userData) {
        User user = findUser(userData.getId());
        if (user != null) {
            user.setName(userData.getName());
            user.setGender(userData.getGender());
            user.setAddress(userData.getAddress());

            User updatedUser = userRepository.save(user);
            ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();
            BeanUtils.copyProperties(updatedUser, resUpdateUserDTO);

            return resUpdateUserDTO;
        }

        return null;
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

    public User findUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

}
