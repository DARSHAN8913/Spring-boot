package net.engineeringdigest.entity;

import jdk.jfr.Unsigned;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
@Entity   // can we interchange the order of this annot?
@Table(name="plans")
@Data
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_name", nullable = false)
    private String planName;

    private Integer inventory_limit;
    private Integer saleLimit;
    private Integer apiLimit;
    private String duration;
    private Double price;

    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
    // below hooks does they execute at the db level or like is there a n+1 execution by app on db
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}