package com.joborbit.repository;

import com.joborbit.entity.JobPost;
import com.joborbit.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    List<JobPost> findByStatus(JobStatus status);

    List<JobPost> findByPostedById(Long recruiterId);

    @Query("SELECT j FROM JobPost j WHERE j.status = 'ACTIVE' AND " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(j.skillsRequired) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<JobPost> searchJobs(@Param("keyword") String keyword, @Param("location") String location);
}
