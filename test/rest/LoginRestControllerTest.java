package rest;

import exceptions.InvalidPasswordException;
import exceptions.InvalidUsernameException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import rest.dto.LoginRequest;
import service.LoginService;

import javax.ws.rs.core.Response;

public class LoginRestControllerTest {

    @InjectMocks private LoginRestController loginRestController;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private LoginService loginService;

    @Test
    public void test_loginAttempt_method(){

        LoginRequest request = new LoginRequest();
        request.setUser("user");
        request.setPassword("password");

        try {
            Mockito.when(loginService.login(request)).thenReturn("1234");
        } catch (InvalidPasswordException | InvalidUsernameException ex) {
            Assert.fail();
        }


        System.out.println(Response.class);
        Response response = loginRestController.loginAttempt(request);
        //System.out.println(response.getStatus());
        //Assert.assertEquals(Response.Status.OK, );
    }
}
