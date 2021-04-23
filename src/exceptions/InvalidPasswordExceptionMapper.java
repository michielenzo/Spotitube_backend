package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidPasswordExceptionMapper implements ExceptionMapper<InvalidPasswordException> {

    @Override
    public Response toResponse(InvalidPasswordException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity("Password is invalid.").build();
    }
}
