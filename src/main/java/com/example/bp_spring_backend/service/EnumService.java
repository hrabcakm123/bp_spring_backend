package com.example.bp_spring_backend.service;

import com.example.bp_spring_backend.domains.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnumService {

    public List<String> getEnumValues(Class<? extends Enum<?>> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    public List<String> getRoleEnumValues() {
        return Arrays.stream(RoleEnum.values())
                .map(Enum::name)
                .filter(value -> !value.equals("SYSTEM"))
                .toList();
    }
}

