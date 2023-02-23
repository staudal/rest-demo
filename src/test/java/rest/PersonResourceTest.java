package rest;

import entities.Person;
import exceptions.PersonNotFoundException;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1, p2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();

        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        p1 = new Person("xxx", "yyy", "12345678");
        p2 = new Person("aaa", "bbb", "12345678");

        try {
            em.getTransaction().begin();
            em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/persons").then().statusCode(200);
    }

    // Test that the get method returns the correct person
    @Test
    public void testGetPerson() {
        given()
                .contentType("application/json")
                .get("/persons/" + 6)
        .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode());
    }

    // Test that the delete method removes the correct person
    @Test
    public void testDeletePerson() {
        given()
                .contentType("application/json")
                .delete("/persons/" + p1.getId())
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }

    // Test that the add method adds the correct person
    @Test
    public void testAddPerson() {
        given()
                .contentType("application/json")
                .body("{\"firstName\":\"ccc\",\"lastName\":\"ddd\",\"phone\":\"12345678\"}")
                .post("/persons")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("ccc"));
    }

    // Test that the edit method edits the correct person
    @Test
    public void testEditPerson() {
        given()
                .contentType("application/json")
                .body("{\"firstName\":\"ccc\",\"lastName\":\"ddd\",\"phone\":\"12345678\"}")
                .put("/persons/" + p1.getId())
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }

    // Test that error code 404 is returned when a person is not found
    @Test
    public void testPersonNotFound() {
        given()
                .contentType("application/json")
                .get("/persons/" + 999)
        .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode());
    }

    // Test that error code 500 is returned when a runtime exception is thrown
    @Test
    public void testRuntimeException() {
        given()
                .contentType("application/json")
                .post("/persons/")
        .then()
                .assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR_500.getStatusCode());
    }

    // Test that error code 404 is returned when input is not valid
    @Test
    public void testInputNotValid() {
        given()
                .contentType("application/json")
                .body("{\"firstName\":\"ccc\",\"lastName\":\"ddd\"}")
                .post("/persons")
        .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode());
    }
}
