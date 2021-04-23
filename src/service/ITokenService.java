package service;

public interface ITokenService {
    String generateToken();
    void validateToken(String token);
}
