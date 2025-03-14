package com.nvc.spring_boot.service;

import com.nvc.spring_boot.dto.skill.request.CreateSkillRequest;
import com.nvc.spring_boot.dto.skill.response.CreateSkillResponse;
import com.nvc.spring_boot.entity.Skill;
import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.repository.SkillRepository;
import com.nvc.spring_boot.util.error.BadRequestException;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public PaginationResponse getList(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> pageSkill = skillRepository.findAll(specification, pageable);
        PaginationResponse.PaginationMeta meta = PaginationResponse.PaginationMeta.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .itemsPerPage(pageable.getPageSize())
                .totalPages(pageSkill.getTotalPages())
                .totalItems(pageSkill.getTotalElements())
                .build();

        return PaginationResponse.builder()
                .meta(meta)
                .content(pageSkill.getContent())
                .build();
    }

    public Skill findSkill(Long id) {
        return skillRepository.findById(id).orElse(null);
    }

    public Skill updateSkill(Skill skillData) throws ResourceNotFoundException, BadRequestException {
        Skill skill = findSkill(skillData.getId());
        if (skill != null) {
            Boolean isDuplicatedSkillName = skillRepository.existsByNameAndIdNot(skillData.getName(), skill.getId());
            if (isDuplicatedSkillName) {
                throw new BadRequestException("Skill name already exists");
            }

            skill.setName(skillData.getName());
            return skillRepository.save(skill);
        } else {
            throw new ResourceNotFoundException("Skill not found");
        }
    }

    public CreateSkillResponse createSkill(CreateSkillRequest skillData) {
        Boolean isSkillExists = skillRepository.existsByName(skillData.getName());
        if (isSkillExists) {
            throw new BadRequestException("Skill name already exists");
        }

        Skill newSkill = Skill.builder().name(skillData.getName()).build();
        skillRepository.save(newSkill);

        CreateSkillResponse resCreateSkill = new CreateSkillResponse();
        BeanUtils.copyProperties(newSkill, resCreateSkill);

        return resCreateSkill;
    }


    public void deleteSkill(Long id) {
        Skill skill = findSkill(id);
        if (skill != null) {
            skill.getJobs().forEach(job -> job.getSkills().remove(skill));
        }
        skillRepository.deleteById(id);
    }
}
