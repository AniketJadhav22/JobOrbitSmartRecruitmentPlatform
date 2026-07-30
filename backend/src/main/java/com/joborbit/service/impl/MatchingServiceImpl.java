package com.joborbit.service.impl;

import com.joborbit.service.MatchingService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchingServiceImpl implements MatchingService {

    /**
     * Smart matching algorithm:
     * Splits both skill lists on commas, normalises case/whitespace, and
     * returns what percentage of the JOB's required skills the CANDIDATE
     * actually has. 100% = candidate has every skill the job asks for.
     */
    @Override
    public double computeMatchScore(String candidateSkillsCsv, String jobSkillsCsv) {
        Set<String> candidateSkills = toSkillSet(candidateSkillsCsv);
        Set<String> jobSkills = toSkillSet(jobSkillsCsv);

        if (jobSkills.isEmpty()) {
            return 0.0;
        }

        long matched = jobSkills.stream().filter(candidateSkills::contains).count();
        double score = (matched * 100.0) / jobSkills.size();
        return Math.round(score * 100.0) / 100.0;
    }

    private Set<String> toSkillSet(String csv) {
        if (csv == null || csv.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
