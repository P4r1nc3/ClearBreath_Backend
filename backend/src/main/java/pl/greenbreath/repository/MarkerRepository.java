package pl.greenbreath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.greenbreath.model.Marker;
import pl.greenbreath.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarkerRepository extends JpaRepository<Marker, Long> {
    List<Marker> findByUser(User user);
    Optional<Marker> findByLatAndLngAndUser(double lat, double lng, User user);
    void deleteByUser(User user);
}
