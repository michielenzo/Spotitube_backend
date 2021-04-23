package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DatabaseExceptionMapper implements ExceptionMapper<DatabaseException> {

    @Override
    public Response toResponse(DatabaseException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("An error occurred communicating with the database.")
                .build();
    }
}
