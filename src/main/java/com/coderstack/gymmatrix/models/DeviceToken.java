package com.coderstack.gymmatrix.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_tokens")
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 512)
    private String token;

    @Column(name = "added_on")
    private LocalDateTime addedOn;

    @Column(name = "last_accessed_on")
    private LocalDateTime lastAccessedOn;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public LocalDateTime getLastAccessedOn() {
        return lastAccessedOn;
    }

    public void setLastAccessedOn(LocalDateTime lastAccessedOn) {
        this.lastAccessedOn = lastAccessedOn;
    }
}
