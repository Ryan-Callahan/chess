package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.interfaces.AuthDAO;

import java.util.UUID;

public class AuthService {
    private final AuthDAO authDAO;
    public AuthService() {
        authDAO = new MemoryAuthDAO();
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
