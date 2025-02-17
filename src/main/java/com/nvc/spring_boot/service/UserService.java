package com.nvc.spring_boot.service;

import java.util.List;

import com.nvc.spring_boot.domain.Company;
import com.nvc.spring_boot.domain.dto.MetaDTO;
import com.nvc.spring_boot.domain.dto.PaginationDTO;
import com.nvc.spring_boot.domain.dto.ResUpdateUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nvc.spring_boot.domain.User;
import com.nvc.spring_boot.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public User updateUser(ResUpdateUserDTO userData) {
        User user = findUser(userData.getId());
        if (user != null) {
            user.setName(userData.getName());
            user.setGender(userData.getGender());
            user.setAddress(userData.getAddress());
            return userRepository.save(user);
        }

        return user;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

}
