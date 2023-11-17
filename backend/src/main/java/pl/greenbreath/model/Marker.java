package pl.greenbreath.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "markers")
public class Marker {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long markerId;
     private double lat;
     private double lng;
     @JsonIgnore
     @ManyToMany(fetch = FetchType.EAGER, mappedBy = "markers", cascade = CascadeType.MERGE)
     private Set<User> users = new HashSet<>();
}
