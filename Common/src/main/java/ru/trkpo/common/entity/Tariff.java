package ru.trkpo.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tariffs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {

    @Id
    @Column(name = "id", length = 2)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "tariff")
    private List<PhoneNumber> phoneNumberList;

    @OneToMany(mappedBy = "tariff")
    private List<TariffConfig> tariffConfigList;
}
