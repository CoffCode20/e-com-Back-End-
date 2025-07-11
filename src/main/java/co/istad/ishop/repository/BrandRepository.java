package co.istad.ishop.repository;

import co.istad.ishop.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    Optional<Brand> findByUuid(String uuid);
    boolean existsBrandsByName(String name);

}
