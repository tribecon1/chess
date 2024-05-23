package response;

public record ErrorResponse(int statusCode, String message) implements ResponseType {}