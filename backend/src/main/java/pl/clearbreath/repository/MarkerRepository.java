package pl.clearbreath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.clearbreath.model.Marker;
import pl.clearbreath.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarkerRepository extends JpaRepository<Marker, Long> {
    List<Marker> findByUser(User user);
    Optional<Marker> findByLatAndLngAndUser(double lat, double lng, User user);
    void deleteByUser(User user);
}
