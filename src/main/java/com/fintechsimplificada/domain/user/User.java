package com.fintechsimplificada.domain.user;

import com.fintechsimplificada.dtos.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name="users")
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String document;

    @Column(unique = true)
    private String email;

    private String password;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserDTO userData) {
        this.firstName = userData.firstName();
        this.lastName = userData.lastName();
        this.balance = userData.balance();
        this.userType = userData.userType();
        this.password = userData.password();
        this.email = userData.email();
        this.document = userData.document();
    }
}
