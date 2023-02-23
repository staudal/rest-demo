package facades;

import dtos.PersonDTO;
import entities.Person;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {}

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
            em.persist(new Person("Kurt", "Wonnegut", "12345678"));
            em.persist(new Person("Hanne", "Olsen", "12345678"));

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {}

    // Check if add person works
    @Test
    public void testAddPerson() throws Exception {
        facade.addPerson("Kurt", "Wonnegut", "12345678");
        assertEquals(3, facade.getAllPersons().size(), "Expects three rows in the database");
    }

    // Check if get all persons works
    @Test
    public void testGetAllPersons() throws Exception {
        assertEquals(2, facade.getAllPersons().size(), "Expects two rows in the database");
    }

    @Test
    public void testGetPerson() throws Exception {
        assertEquals("Kurt", facade.getPerson(1).getFirstName(), "Expects Kurt");
    }

    @Test
    public void testDeletePerson() throws Exception {
        facade.deletePerson(1);
        assertEquals(1, facade.getAllPersons().size(), "Expects one row in the database");
    }

    @Test
    public void testEditPerson() throws Exception {
        PersonDTO personDTO = facade.getPerson(1);
        personDTO.setFirstName("Jakob");
        facade.editPerson(personDTO);
        assertEquals("Jakob", facade.getPerson(1).getFirstName(), "Expects Jakob");
    }
}
