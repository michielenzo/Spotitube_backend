package service;

import datasource.datamapper.OwnerDataMapper;
import domain.objects.Owner;
import exceptions.InvalidPasswordException;
import exceptions.InvalidUsernameException;
import rest.dto.LoginRequest;

import javax.inject.Inject;

public class LoginService implements ILoginService {

    private TokenService tokenService;

    private OwnerDataMapper ownerDataMapper;

    @Inject
    public LoginService(TokenService tokenService, OwnerDataMapper ownerDataMapper) {
        this.tokenService = tokenService;
        this.ownerDataMapper = ownerDataMapper;
    }

    @Override
    public String login(LoginRequest loginRequest) throws InvalidPasswordException, InvalidUsernameException {
        Owner owner = ownerDataMapper.read(loginRequest.getUser());
        validateLoginRequest(owner, loginRequest);
        String token = tokenService.generateToken();
        ownerDataMapper.update(owner.getUsername(), owner.getPassword(), token);
        return token;
    }

    private void validateLoginRequest(Owner owner, LoginRequest loginRequest)
            throws InvalidUsernameException, InvalidPasswordException
    {
        if(owner.getUsername() == null){
            throw new InvalidUsernameException();
        }else if(!owner.getPassword().equals(loginRequest.getPassword())){
            throw new InvalidPasswordException();
        }
    }
}
