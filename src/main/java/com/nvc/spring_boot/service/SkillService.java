package com.nvc.spring_boot.service;

import com.nvc.spring_boot.domain.Skill;
import com.nvc.spring_boot.domain.response.PaginationDTO;
import com.nvc.spring_boot.repository.SkillRepository;
import com.nvc.spring_boot.util.error.BadRequestException;
import com.nvc.spring_boot.util.error.ResourceNotFoundException;
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

    public PaginationDTO getList(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> pageCompany = skillRepository.findAll(specification, pageable);
        PaginationDTO result = new PaginationDTO();
        PaginationDTO.MetaDTO meta = new PaginationDTO.MetaDTO();

        meta.setCurrentPage(pageable.getPageNumber() + 1);
        meta.setItemsPerPage(pageable.getPageSize());
        meta.setTotalPages(pageCompany.getTotalPages());
        meta.setTotalItems(pageCompany.getTotalElements());

        result.setMeta(meta);
        result.setContent(pageCompany.getContent());

        return result;
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

    public Skill createSkill(Skill skill) {
        Boolean isSkillExists = skillRepository.existsByName(skill.getName());
        if (isSkillExists) {
            throw new BadRequestException("Skill name already exists");
        }

        return skillRepository.save(skill);
    }

    public void deleteSkill(Long id) {
        Skill skill = findSkill(id);
        if (skill != null) {
            skill.getJobs().forEach(job -> job.getSkills().remove(skill));
        }
        skillRepository.deleteById(id);
    }
}
