package pl.clearbreath.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
     @JsonIgnore
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long markerId;
     private double lat;
     private double lng;
     private double latStation;
     private double lngStation;
     private double distance;
     private String continent;
     private String countryName;
     private String city;
     @JsonIgnore
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "user_id")
     private User user;
}
