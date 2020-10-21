package service;

import datasource.datamapper.OwnerDataMapper;
import domain.objects.Owner;
import exceptions.InvalidPasswordException;
import exceptions.InvalidUsernameException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import rest.dto.LoginRequest;

public class LoginServiceTest {

    private LoginService loginService;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private TokenService tokenService;
    @Mock private OwnerDataMapper ownerDataMapper;

    @Before
    public void initialize(){
        loginService = new LoginService(tokenService, ownerDataMapper);
    }

    @Test
    public void test_login_method(){
        Owner owner = new Owner();
        owner.setPassword("1234");
        owner.setUsername("Henk");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("1234");
        loginRequest.setUser("Henk");

        Mockito.when(ownerDataMapper.read(owner.getUsername())).thenReturn(owner);
        Mockito.when(tokenService.generateToken()).thenReturn("token");

        try{
            String token = loginService.login(loginRequest);
            Assert.assertEquals("token", token);
        } catch (InvalidPasswordException | InvalidUsernameException ex) {
            Assert.fail();
        }

        Mockito.verify(ownerDataMapper).update(owner.getUsername(), owner.getPassword(), "token");
    }

    @Test
    public void test_login_methodThrowsException(){
        Owner owner = new Owner();
        owner.setPassword("1234");
        owner.setUsername("Henk");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("5678");
        loginRequest.setUser("Henk");

        Mockito.when(ownerDataMapper.read(owner.getUsername())).thenReturn(owner);
        Mockito.when(tokenService.generateToken()).thenReturn("token");

        try{
            loginService.login(loginRequest);
            Assert.fail();
        } catch (InvalidPasswordException | InvalidUsernameException ex) {
            if(ex instanceof InvalidPasswordException){
                Assert.assertTrue(true);
            } else{
                Assert.fail();
            }
        }
    }
}
