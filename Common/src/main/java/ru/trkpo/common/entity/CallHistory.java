package ru.trkpo.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "call_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_phone_number_id", referencedColumnName = "client_id")
    private PhoneNumber phoneNumber;

    @ManyToOne
    @JoinColumn(name = "call_type_id", referencedColumnName = "id")
    private CallType callType;

    @Column(name = "date_start")
    private LocalDateTime dateStart;

    @Column(name = "date_end")
    private LocalDateTime dateEnd;

    @Column(name = "cost")
    private BigDecimal cost;
}
