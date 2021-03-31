package cuongnguyen.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String passWord;

    @Column(name = "firts_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "avt")
    private String avt;

    public String fullName(){
        return new StringBuilder(lastName).append(" ").append(firstName).toString();
    }
}
