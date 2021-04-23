package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidTokenExceptionMapper implements ExceptionMapper<InvalidTokenException> {

    @Override
    public Response toResponse(InvalidTokenException e) {
        return Response.status(Response.Status.FORBIDDEN).entity("The token provided is invalid.").build();
    }
}
