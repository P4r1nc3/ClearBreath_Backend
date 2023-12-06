package pl.greenbreath.model;

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
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long markerId;
     private double lat;
     private double lng;
     private String continent;
     private String countryName;
     private String city;
     @JsonIgnore
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "user_id")
     private User user;
}
