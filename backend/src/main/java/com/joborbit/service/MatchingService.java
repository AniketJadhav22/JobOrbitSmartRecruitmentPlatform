package com.joborbit.service;

/**
 * Core "smart" component of the platform: computes a percentage match
 * score between a candidate's skill set and a job's required skills.
 */
public interface MatchingService {
    double computeMatchScore(String candidateSkillsCsv, String jobSkillsCsv);
}
