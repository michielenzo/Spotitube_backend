package rest;

import rest.dto.LoginRequest;
import rest.dto.LoginResponse;
import service.LoginService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class LoginRestController {

    @Inject public LoginService loginService;

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginAttempt(LoginRequest loginRequest){
        String token = loginService.login(loginRequest);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(loginRequest.getUser());

        return Response.status(Response.Status.OK).entity(response).build();
    }
}
