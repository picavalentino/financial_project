package com.team.financial_project.customer.enums;

import lombok.Getter;

@Getter
public enum CustomerStatus {
    ACTIVE(1),
    INACTIVE(2);

    private final int value;

    CustomerStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CustomerStatus fromValue(int value) {
        for (CustomerStatus status : CustomerStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
