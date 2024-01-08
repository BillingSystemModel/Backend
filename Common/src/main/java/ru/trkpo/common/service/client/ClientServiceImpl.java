package ru.trkpo.common.service.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.trkpo.common.data.entity.Client;
import ru.trkpo.common.exception.NoDataFoundException;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client createNewClient(String[] fio) {
        if (fio.length < 2)
            throw new NoDataFoundException("FIO field is empty");
        return Client.builder()
                .id(null)
                .firstName(fio[0])
                .lastName(fio[1])
                .patronymic(fio.length == 3 ? fio[2] : null)
                .age(null)
                .birthday(null)
                .phoneNumber(null)
                .clientDetails(null)
                .build();
    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

}
