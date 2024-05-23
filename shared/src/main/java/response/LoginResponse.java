package response;

public record LoginResponse(String username, String authToken) implements ResponseType {}
//also works for RegistrationResponse