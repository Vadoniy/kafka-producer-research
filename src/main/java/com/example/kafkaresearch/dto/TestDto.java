package com.example.kafkaresearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TestDto {

    private long id;

    private String name;

    @Override
    public String toString() {
        return "TestDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
