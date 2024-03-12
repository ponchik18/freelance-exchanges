package com.bsuir.service;

import com.bsuir.entity.Freelancer;
import com.bsuir.exception.DuplicateEmailException;
import com.bsuir.exception.FreelancerNotFoundException;
import com.bsuir.repository.FreelancerRepository;
import com.bsuir.util.PageSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreelancerService {
    private final FreelancerRepository freelancerRepository;

    public void validateExistById(long id) {
        if (!freelancerRepository.existsById(id)) {
            throw new FreelancerNotFoundException(id);
        }
    }

    public Page<Freelancer> getAll(PageSetting pageSetting) {
        Pageable pageable = PageRequest.of(pageSetting.getPageNumber(), pageSetting.getElementsPerPage());

        return freelancerRepository.findAll(pageable);
    }

    public Freelancer getById(long id) {
        return freelancerRepository.findById(id)
                .orElseThrow(() -> new FreelancerNotFoundException(id));
    }

    public Freelancer save(Freelancer createdFreelancer) {
        String email = createdFreelancer.getEmail();
        if (freelancerRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }

        return freelancerRepository.save(createdFreelancer);
    }

    public Freelancer update(long id, Freelancer updatedFreelancer) {
        String email = updatedFreelancer.getEmail();
        freelancerRepository.findByEmailAndIdNot(email, id)
                .ifPresent(existingFreelancer -> {
                    throw new DuplicateEmailException(email);
                });

        Freelancer freelancer = getById(id);
        updateField(freelancer, updatedFreelancer);
        return freelancerRepository.save(freelancer);
    }

    private void updateField(Freelancer freelancer, Freelancer updatedFreelancer) {
        freelancer.setFirstName(updatedFreelancer.getFirstName());
        freelancer.setLastName(updatedFreelancer.getLastName());
        freelancer.setProfilePicture(updatedFreelancer.getProfilePicture());
        freelancer.setEmail(updatedFreelancer.getEmail());
    }
}