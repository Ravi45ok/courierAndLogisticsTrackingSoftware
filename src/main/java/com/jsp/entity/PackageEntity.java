package com.jsp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "package")
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageType packageType;

    @Column(nullable = false)
    private Boolean fragile;

    // Stored as "LxBxH" string format
    @Column(nullable = false)
    private String dimension;
    
    @OneToOne(mappedBy = "packageEntity")
    @JsonIgnore
    private Shipment shipment;
}
