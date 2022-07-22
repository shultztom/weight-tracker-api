package com.shultzlab.weighttrackerapi.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    // Save height as cm
    private Double height;

    @Column
    // Use local date
    private LocalDate birthday;

    @Column
    private String activityLevel;

    @Column
    private String gender;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedAt")
    private Date modifiedAt;

    public User(String username, Double height, LocalDate birthday, String activityLevel, String gender, Date createdAt, Date modifiedAt) {
        this.username = username;
        this.height = height;
        this.birthday = birthday;
        this.activityLevel = activityLevel;
        this.gender = gender;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public User() {

    }
}
