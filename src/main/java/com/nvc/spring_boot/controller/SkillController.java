package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.dto.skill.request.CreateSkillRequest;
import com.nvc.spring_boot.dto.skill.request.UpdateSkillRequest;
import com.nvc.spring_boot.dto.skill.response.CreateSkillResponse;
import com.nvc.spring_boot.dto.skill.response.FindSkillResponse;
import com.nvc.spring_boot.dto.skill.response.UpdateSkillResponse;
import com.nvc.spring_boot.entity.Skill;
import com.nvc.spring_boot.dto.PaginationResponse;
import com.nvc.spring_boot.service.SkillService;
import com.nvc.spring_boot.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/list")
    @ApiMessage("get skill list")
    public ResponseEntity<PaginationResponse> getList(
            @Filter Specification<Skill> specification,
            Pageable pageable) {
        return ResponseEntity.ok(skillService.getList(specification, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("get skill info")
    public ResponseEntity<FindSkillResponse> findSkill(@PathVariable("id") Long id) {
        return ResponseEntity.ok(skillService.findSkill(id));
    }

    @PostMapping()
    @ApiMessage("create new skill")
    public ResponseEntity<CreateSkillResponse> createSkill(@Valid @RequestBody CreateSkillRequest skill) {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.createSkill(skill));
    }

    @PutMapping()
    @ApiMessage("update skill info")
    public ResponseEntity<UpdateSkillResponse> updateSkill(@Valid @RequestBody UpdateSkillRequest skill) {
        return ResponseEntity.ok(skillService.updateSkill(skill));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
