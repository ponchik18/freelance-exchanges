package com.bsuir.dto.skill;

import com.bsuir.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillWithParentResponse {
    private Skill parentSkill;
    private List<Skill> skills;
}
