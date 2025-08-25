package com.example.bp_spring_backend.domains.inputDTO;

import com.example.bp_spring_backend.validation.OnCreate;
import com.example.bp_spring_backend.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequestDTO {

    @NotNull(groups = OnCreate.class, message = "blockId is required")
    @Positive(groups = OnCreate.class, message = "blockId must be positive")
    @Positive(groups = OnUpdate.class, message = "blockId must be positive")
    private Integer blockId;
    @NotBlank(groups = OnCreate.class, message = "name is required") // cannot be null and blank
    @Pattern(groups = OnUpdate.class, regexp = "^\\s*\\S.*$", message = "name must not be blank") // can be null
    private String name;
    @Pattern(groups = OnUpdate.class, regexp = "^\\s*\\S.*$", message = "note must not be blank")
    private String note;
    @NotNull(groups = OnCreate.class, message = "maxPoints is required")
    @Positive(groups = OnCreate.class, message = "maxPoints must be positive")
    @Positive(groups = OnUpdate.class, message = "maxPoints must be positive")
    private Double maxPoints;
}
