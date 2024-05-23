package model;

import response.ResponseType;

public record AuthData(String authToken, String username) implements ResponseType {}
