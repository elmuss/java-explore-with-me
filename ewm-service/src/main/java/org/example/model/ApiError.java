package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class ApiError {
    private ErrorStatus status;
    private String reason;
    private String message;
    private String timestamp;
}
