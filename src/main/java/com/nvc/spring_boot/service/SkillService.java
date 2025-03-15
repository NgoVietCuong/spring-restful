package com.nvc.spring_boot.service;

import com.nvc.spring_boot.dto.skill.request.CreateSkillRequest;
import com.nvc.spring_boot.dto.skill.request.UpdateSkillRequest;
import com.nvc.spring_boot.dto.skill.response.CreateSkillResponse;
import com.nvc.spring_boot.dto.skill.response.FindSkillResponse;
import com.nvc.spring_boot.dto.skill.response.UpdateSkillResponse;
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

import java.util.List;

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

        List<FindSkillResponse> resSkills = pageSkill.getContent().stream().map(item -> {
            FindSkillResponse resSkill = new FindSkillResponse();
            BeanUtils.copyProperties(item, resSkill);

            return resSkill;
        }).toList();

        return PaginationResponse.builder()
                .meta(meta)
                .content(resSkills)
                .build();
    }

    public FindSkillResponse findSkill(Long id) {
        Skill skill = skillRepository.findById(id).orElse(null);

        if (skill != null) {
            FindSkillResponse resFindSkill = new FindSkillResponse();
            BeanUtils.copyProperties(skill, resFindSkill);

            return resFindSkill;
        }

        return null;
    }

    public UpdateSkillResponse updateSkill(UpdateSkillRequest skillData) throws ResourceNotFoundException, BadRequestException {
        Skill skill = skillRepository.findById(skillData.getId()).orElse(null);
        if (skill != null) {
            Boolean isDuplicatedSkillName = skillRepository.existsByNameAndIdNot(skillData.getName(), skill.getId());
            if (isDuplicatedSkillName) {
                throw new BadRequestException("Skill name already exists");
            }

            skill.setName(skillData.getName());
            skillRepository.save(skill);

            UpdateSkillResponse resUpdateSkill = new UpdateSkillResponse();
            BeanUtils.copyProperties(skill, resUpdateSkill);

            return resUpdateSkill;
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
        Skill skill = skillRepository.findById(id).orElse(null);
        if (skill != null) {
            skill.getJobs().forEach(job -> job.getSkills().remove(skill));
        }
        skillRepository.deleteById(id);
    }
}
