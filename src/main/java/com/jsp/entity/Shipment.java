package com.jsp.entity;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String trackingNumber;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime shipmentDateTime;

    // Format: YYYY-MM-DD
    @Column(nullable = false)
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status;
    
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name="deliveryAgent_id")
    private  DeliveryAgent deliveryAgent;
    
    @ManyToOne
    @JoinColumn(name="warehouse_id")
    private Warehouse warehouse;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="payment_id")
    private Payment payment;
    
    @OneToMany(mappedBy = "shipment",cascade = CascadeType.ALL)
    private List<TrackingHistory> trackingHistory;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "package_id")
    private PackageEntity packageEntity;
}
