package com.github.not.n0w.islab1.model.ext;

import java.io.Serializable;

public record MinimalPointCount(Double minimalPoints, Integer count) implements MinimalPointGroup, Serializable {
    @Override
    public Double getMinimalPoints() {
        return minimalPoints;
    }

    @Override
    public Integer getCount() {
        return count;
    }
}