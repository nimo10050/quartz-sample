package com.nimo.demo02.repository;

import com.nimo.demo02.entity.JobEntity;
import org.springframework.data.repository.CrudRepository;

public interface JobEntityRepository extends CrudRepository<JobEntity, Long> {

    JobEntity getById(Integer id);
}
