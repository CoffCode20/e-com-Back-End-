package co.istad.ishop.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    private String profilePicture;

    private Boolean isVerified;

    private Boolean isActive;

    private Boolean isDeleted;

    private Date createdDate;

    @ManyToMany(targetEntity = Role.class)
    private Set<Role> roles;


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(e -> new SimpleGrantedAuthority("ROLE_" + e.getName()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public String getUsername() {
//        return this.username;
//    }

}
