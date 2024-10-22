package model.request;

public record RequestWithAuth(String authToken, Object request) {
}
