package exceptions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MissingInputExceptionMapper implements ExceptionMapper<MissingInputException> {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Context
    ServletContext context;

    @Override
    public Response toResponse(MissingInputException e) {
        Logger.getLogger(exceptions.PersonNotFoundExceptionMapper.class.getName()).log(Level.SEVERE, null, e);
        ExceptionDTO err = new ExceptionDTO(404, e.getMessage());

        return Response.status(404)
                .entity(gson.toJson(err))
                .type(MediaType.APPLICATION_JSON).
                build();
    }
}
