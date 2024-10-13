package dominikschweigl.github.dto;

public record Verification(
        boolean verified,
        String reason,
        String signature,
        String payload
) {}