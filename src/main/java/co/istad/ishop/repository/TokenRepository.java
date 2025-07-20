package co.istad.ishop.repository;

import co.istad.ishop.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    List<Token> findByUsernameAndTypeAndValid(String username, String type, boolean valid);
    Optional<Token> findByTokenAndValid(String token, boolean valid);
}
