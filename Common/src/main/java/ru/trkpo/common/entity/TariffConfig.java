package ru.trkpo.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tariffs_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tariff_id", referencedColumnName = "id")
    private Tariff tariff;

    @ManyToOne
    @JoinColumn(name = "telephony_package_id", referencedColumnName = "package_id")
    private TelephonyPackage telephonyPackage;

    @ManyToOne
    @JoinColumn(name = "internet_package_id", referencedColumnName = "package_id")
    private InternetPackage internetPackage;
}
