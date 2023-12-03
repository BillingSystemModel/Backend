package ru.trkpo.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "telecom_operators")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelecomOperator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "operator")
    private List<TelephonyPackage> telephonyPackageList;
}
