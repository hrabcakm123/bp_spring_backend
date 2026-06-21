package com.example.bp_spring_backend.feature.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enum")
@RequiredArgsConstructor
public class EnumController {

    private final EnumService enumService;

    @GetMapping("/attendance")
    public List<String> getAttendanceEnums() {
        return enumService.getEnumValues(AttendanceEnum.class);
    }

    @GetMapping("/room")
    public List<String> getRoomEnums() {
        return enumService.getEnumValues(RoomEnum.class);
    }

    @GetMapping("/role")
    public List<String> getRoleEnums() {
        return enumService.getRoleEnumValues();
    }
}