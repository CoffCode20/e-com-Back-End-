package co.istad.ishop.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Token {
    @Id
    @Column(columnDefinition = "TEXT")
    private String token;
    private String username;
    private String type; // type of token
    private long expiration;
    private boolean valid;
}
