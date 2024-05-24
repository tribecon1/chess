package model;

import response.ResponseType;

public record AuthData(String username, String authToken) implements ResponseType {}
