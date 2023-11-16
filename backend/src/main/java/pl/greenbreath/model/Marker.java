package pl.greenbreath.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Marker {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long markerId;
     private double lat;
     private double lng;
}
