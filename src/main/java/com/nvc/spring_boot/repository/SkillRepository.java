package com.nvc.spring_boot.repository;

import com.nvc.spring_boot.domain.Skill;
import com.nvc.spring_boot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {
    Boolean existsByName(String name);
    Boolean existsByNameAndIdNot(String name, Long id);
    List<Skill> findSkillByIdIn(Collection<Long> ids);
}
