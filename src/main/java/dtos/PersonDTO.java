/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PersonDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phone;

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phone = person.getPhone();
    }

    public PersonDTO(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public static List<PersonDTO> getDTOs(List<Person> persons){
        List<PersonDTO> personDTOs = new ArrayList();
        persons.forEach(person->personDTOs.add(new PersonDTO(person)));
        return personDTOs;
    }
}
