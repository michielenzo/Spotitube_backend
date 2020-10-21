package service;

import exceptions.InvalidPasswordException;
import exceptions.InvalidUsernameException;
import rest.dto.LoginRequest;

public interface ILoginService {
    String login(LoginRequest loginRequest) throws InvalidPasswordException, InvalidUsernameException;
}
