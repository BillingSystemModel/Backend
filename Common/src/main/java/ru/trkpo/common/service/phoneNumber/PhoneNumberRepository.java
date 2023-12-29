package ru.trkpo.common.service.phoneNumber;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trkpo.common.data.entity.PhoneNumber;

import java.util.Optional;

@Repository
public interface PhoneNumberRepository extends CrudRepository<PhoneNumber, Long> {

    Page<PhoneNumber> findAll(Pageable pageable);
  
    Optional<PhoneNumber> findByPhoneNumber(String phoneNumber);
}
