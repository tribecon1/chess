package response;

import model.AuthData;

public record RegisterResponse(AuthData authData) implements ResponseType {}
