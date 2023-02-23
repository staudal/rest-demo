package facades;

import dtos.PersonDTO;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;

import java.util.List;

public interface IPersonFacade {
    public PersonDTO addPerson(String fName, String lName, String phone) throws MissingInputException;
    public void deletePerson(int id) throws PersonNotFoundException;
    public PersonDTO getPerson(int id) throws PersonNotFoundException;
    public List<PersonDTO> getAllPersons();
    public void editPerson(PersonDTO p) throws PersonNotFoundException, MissingInputException;
}
