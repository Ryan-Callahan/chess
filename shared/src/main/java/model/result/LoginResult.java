package model.result;

public record LoginResult(int statusCode, String username, String authToken) {
}
