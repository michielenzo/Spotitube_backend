package service;

public interface ITokenService {
    String generateToken();
    boolean validateToken(String token);
}
