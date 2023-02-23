package facades;

import dtos.PersonDTO;
import entities.Person;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

//import errorhandling.RenameMeNotFoundException;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone) throws MissingInputException {

        if (fName == null || fName.isEmpty() || lName == null || lName.isEmpty() || phone == null || phone.isEmpty()) {
            throw new MissingInputException("First name, last name and phone number must be provided");
        }

        EntityManager em = emf.createEntityManager();
        Person person = new Person(fName, lName, phone);

        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        return new PersonDTO(person);
    }

    @Override
    public void deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, id);

        if (person == null) {
            throw new PersonNotFoundException("Could not delete, provided id does not exist");
        }

        try {
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, id);
        if (person == null) {
            throw new PersonNotFoundException("Could not find person with id: " + id);
        }

        return new PersonDTO(person);
    }

    @Override
    public List<PersonDTO> getAllPersons() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = query.getResultList();
        List<PersonDTO> personDTOs = PersonDTO.getDTOs(persons);
        return personDTOs;
    }

    @Override
    public void editPerson(PersonDTO p) throws PersonNotFoundException, MissingInputException {

        if (p.getFirstName().isEmpty() || p.getLastName().isEmpty() || p.getPhone().isEmpty()) {
            throw new MissingInputException("Could not edit, missing input");
        }

        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, p.getId());

        if (person == null) {
            throw new PersonNotFoundException("Could not edit, provided id does not exist");
        }

        try {
            em.getTransaction().begin();
            person.setFirstName(p.getFirstName());
            person.setLastName(p.getLastName());
            person.setPhone(p.getPhone());
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
