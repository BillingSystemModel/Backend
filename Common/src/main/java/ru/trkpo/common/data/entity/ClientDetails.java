package ru.trkpo.common.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "client_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDetails {

    @Id
    private Long id;

    @Column(name = "number_personal_account", nullable = false, unique = true)
    private Integer numberPersonalAccount;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "passport", length = 10, nullable = false, unique = true)
    private String passport;

    @Column(name = "contract_date")
    private LocalDate contractDate;

    @Column(name = "contract_number", length = 15, nullable = false, unique = true)
    private String contractNumber;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
}
