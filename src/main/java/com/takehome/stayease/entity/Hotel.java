package com.takehome.stayease.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String location;

    private String description; 
    
    @Column(nullable = false)
    private Integer totalRooms;
    
    private Integer availableRooms;

    @OneToMany(mappedBy = "hotel")
    private List<Booking> bookings = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (availableRooms == null) {
            availableRooms = totalRooms;
        }
    }
}
