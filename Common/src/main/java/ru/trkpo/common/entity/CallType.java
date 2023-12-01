package ru.trkpo.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "call_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallType {

    @Id
    private String id;

    @Column(name = "type", length = 20, nullable = false)
    private String type;

    @OneToMany(mappedBy = "callType")
    private List<CallHistory> callHistoryList;

    @OneToMany(mappedBy = "callType")
    private List<TelephonyPackage> telephonyPackageList;
}
