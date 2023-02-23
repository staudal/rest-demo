package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
@NamedQuery(name = "RenameMe.deleteAllRows", query = "DELETE from Person")
@Table(name = "person")
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created")
    private String created;

    @Column(name = "lastEdited")
    private String lastEdited;

    public Person(String firstName, String lastName, String phone) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.created = dateFormat.format(new Date());
        this.lastEdited = dateFormat.format(new Date());
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.lastEdited = dateFormat.format(new Date());
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.lastEdited = dateFormat.format(new Date());
    }

    public void setPhone(String phone) {
        this.phone = phone;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.lastEdited = dateFormat.format(new Date());
    }
}
