package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import utils.EMF_Creator;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("persons")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final PersonFacade FACADE =  PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    // Get person from DB by ID
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonById(@PathParam("id") int id) throws PersonNotFoundException {
        PersonDTO personDTO = FACADE.getPerson(id);
        return GSON.toJson(personDTO);
    }

    // Get all persons from DB
    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() {
        return GSON.toJson(FACADE.getAllPersons());
    }

    // Add person to DB
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response postExample(String input) throws MissingInputException {
        System.out.println("input = " + input);
        PersonDTO personDTO = GSON.fromJson(input, PersonDTO.class);
        FACADE.addPerson(personDTO.getFirstName(), personDTO.getLastName(), personDTO.getPhone());
        return Response.ok().entity(personDTO).build();
    }

    // Delete person from DB
    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deletePerson(@PathParam("id") int id) throws PersonNotFoundException {
        FACADE.deletePerson(id);
        return "{\"msg\":\"Person deleted\"}";
    }

    // Edit person in DB
    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String editPerson(@PathParam("id") int id, String input) throws MissingInputException, PersonNotFoundException {
        PersonDTO personDTO = GSON.fromJson(input, PersonDTO.class);
        personDTO.setId(id);
        FACADE.editPerson(personDTO);
        return "{\"msg\":\"Person edited\"}";
    }
}
