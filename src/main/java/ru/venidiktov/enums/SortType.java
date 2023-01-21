package ru.venidiktov.enums;

import org.springframework.data.domain.Sort;

public enum SortType {
    ASC(false, Sort.Direction.ASC),
    DECS(true, Sort.Direction.DESC);
    private boolean desc;
    private Sort.Direction directionSort;

    SortType(boolean desc, Sort.Direction directionSort) {
        this.desc = desc;
        this.directionSort = directionSort;
    }

    public boolean getDesc() {
        return desc;
    }

    public Sort.Direction getDirectionSort() {
        return directionSort;
    }
}
