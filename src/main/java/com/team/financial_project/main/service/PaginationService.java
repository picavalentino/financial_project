package com.team.financial_project.main.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {
    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumber(int currentPageNumber, int totalPageNumber) {
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 1);
        int endNumber = Math.min(startNumber + BAR_LENGTH - 1, totalPageNumber);

        if (endNumber - startNumber < BAR_LENGTH) {
            startNumber = Math.max(endNumber - BAR_LENGTH + 1, 1);
        }

        return IntStream.rangeClosed(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }
}
