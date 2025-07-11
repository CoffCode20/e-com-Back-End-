package co.istad.ishop.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(indexes = @Index(columnList = "otp"))
public class Otp {
    @Id
    private String otp;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
