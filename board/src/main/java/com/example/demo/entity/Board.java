package com.example.demo.entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private Timestamp joinDate;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String roles;

    @Column(nullable = false)
    private String platform;
}
