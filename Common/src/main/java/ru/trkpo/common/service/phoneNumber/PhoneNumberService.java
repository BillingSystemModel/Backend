package ru.trkpo.common.service.phoneNumber;

import ru.trkpo.common.data.entity.PhoneNumber;
import ru.trkpo.common.data.entity.Tariff;

import java.util.Optional;

public interface PhoneNumberService {

    PhoneNumber findByPhoneNumber(String phoneNumber);
    PhoneNumber findRandom();
    Tariff findActiveTariff(String phoneNumber);
}
