package com.example.recipea.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Mahdi Sharifi
 */
@MappedSuperclass
@Data
@NoArgsConstructor
public class BaseEntity implements Serializable {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}