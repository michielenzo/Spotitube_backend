package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidUsernameExceptionMapper implements ExceptionMapper<InvalidUsernameException> {

    @Override
    public Response toResponse(InvalidUsernameException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity("Username is invalid.").build();
    }
}
