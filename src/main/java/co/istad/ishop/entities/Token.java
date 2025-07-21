package co.istad.ishop.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", unique = true)
    private String token;
    private String username;
    private String type; // type of token
    private long expiration;
    private boolean valid;
}
