/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import javax.persistence.EntityManagerFactory;

import exceptions.MissingInputException;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate() throws MissingInputException {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = PersonFacade.getPersonFacade(emf);
        fe.addPerson("Kurt", "Wonnegut", "12345678");
        fe.addPerson("Hanne", "Olsen", "12345678");
    }
    
    public static void main(String[] args) throws MissingInputException {
        populate();
    }
}
