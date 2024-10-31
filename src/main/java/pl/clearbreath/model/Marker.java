package pl.clearbreath.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "markers")
public class Marker {
     @Id
     @JsonIgnore
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long markerId;

     private LocalDateTime createdAt;
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


     public Long getMarkerId() {
          return markerId;
     }

     public void setMarkerId(Long markerId) {
          this.markerId = markerId;
     }

     public LocalDateTime getCreatedAt() {
          return createdAt;
     }

     public void setCreatedAt(LocalDateTime createdAt) {
          this.createdAt = createdAt;
     }

     public double getLat() {
          return lat;
     }

     public void setLat(double lat) {
          this.lat = lat;
     }

     public double getLng() {
          return lng;
     }

     public void setLng(double lng) {
          this.lng = lng;
     }

     public double getLatStation() {
          return latStation;
     }

     public void setLatStation(double latStation) {
          this.latStation = latStation;
     }

     public double getLngStation() {
          return lngStation;
     }

     public void setLngStation(double lngStation) {
          this.lngStation = lngStation;
     }

     public double getDistance() {
          return distance;
     }

     public void setDistance(double distance) {
          this.distance = distance;
     }

     public String getContinent() {
          return continent;
     }

     public void setContinent(String continent) {
          this.continent = continent;
     }

     public String getCountryName() {
          return countryName;
     }

     public void setCountryName(String countryName) {
          this.countryName = countryName;
     }

     public String getCity() {
          return city;
     }

     public void setCity(String city) {
          this.city = city;
     }

     public User getUser() {
          return user;
     }

     public void setUser(User user) {
          this.user = user;
     }
}
