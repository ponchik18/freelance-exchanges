package com.bsuir.util;

import com.bsuir.dto.job.JobFilter;
import com.bsuir.entity.Job;
import com.bsuir.entity.Skill;
import com.bsuir.enums.JobStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class JobSpecification {

    public static Specification<Job> createSpecification(final JobFilter jobFilter) {
        Specification<Job> jobSpecification = isCreated();

        if (jobFilter == null) {
            return isCreated();
        }

        if (jobFilter.getSearch() != null && !jobFilter.getSearch().isEmpty()) {
            jobSpecification = jobSpecification.and(bySearch(jobFilter.getSearch()));
        }
        if (jobFilter.getStartBudget() != null) {
            jobSpecification = jobSpecification.and(byStartBudget(jobFilter.getStartBudget()));
        }
        if (jobFilter.getEndBudget() != null) {
            jobSpecification = jobSpecification.and(byEndBudget(jobFilter.getEndBudget()));
        }
        if (jobFilter.getSkillIds() != null && !jobFilter.getSkillIds().isEmpty()) {
            jobSpecification = jobSpecification.and(bySkills(jobFilter.getSkillIds()));
        }
        if (jobFilter.getSort() != null) {
            jobSpecification = switch (jobFilter.getSort()) {
                case NEW -> jobSpecification.and(sortByNew());
                case OLD -> jobSpecification.and(sortByOld());
                case BUDGET_ASC -> jobSpecification.and(sortByBudgetAsc());
                case BUDGET_DESC -> jobSpecification.and(sortByBudgetDesc());
            };
        }

        return jobSpecification;
    }

    private static Specification<Job> isCreated() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("status"),
                JobStatus.CREATED
        );
    }

    private static Specification<Job> bySearch(String search) {
        String convertedSearch = ("%" + search + "%").toLowerCase();
        return (root, query, criteriaBuilder) -> {
            Join<Job, Skill> skillsJoin = root.join("skills");
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(skillsJoin.get("name")), convertedSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), convertedSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), convertedSearch)
            );
        };
    }

    private static Specification<Job> byStartBudget(BigDecimal startBudget) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("budget"), startBudget);
    }

    private static Specification<Job> byEndBudget(BigDecimal endBudget) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("budget"), endBudget);
    }

    private static Specification<Job> bySkills(List<Long> skillIds) {
        return (root, query, criteriaBuilder) -> {
            Join<Job, Skill> skillsJoin = root.join("skills");

            Predicate[] predicates = skillIds.stream()
                    .map(skillId -> criteriaBuilder.equal(skillsJoin.get("id"), skillId))
                    .toArray(Predicate[]::new);

            return criteriaBuilder.or(predicates);
        };
    }

    private static Specification<Job> sortByNew() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<Job> sortByOld() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("createdAt")));
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<Job> sortByBudgetAsc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("budget")));
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<Job> sortByBudgetDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("budget")));
            return criteriaBuilder.conjunction();
        };
    }
}