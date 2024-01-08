package ru.trkpo.common.service.client;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trkpo.common.data.entity.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
}
