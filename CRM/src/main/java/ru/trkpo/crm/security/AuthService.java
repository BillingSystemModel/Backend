package ru.trkpo.crm.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.trkpo.common.data.entity.Client;
import ru.trkpo.common.data.entity.ClientDetails;
import ru.trkpo.common.data.entity.PhoneNumber;
import ru.trkpo.common.service.client.ClientService;
import ru.trkpo.common.service.clientDetails.ClientDetailsService;
import ru.trkpo.common.service.phoneNumber.PhoneNumberService;
import ru.trkpo.crm.security.config.JWTService;
import ru.trkpo.crm.security.data.auth.AuthRequest;
import ru.trkpo.crm.security.data.auth.RegisterRequest;
import ru.trkpo.crm.security.data.user.User;
import ru.trkpo.crm.security.data.user.UserRepository;
import ru.trkpo.crm.security.data.user.UserRole;


@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    private final ClientService clientService;
    private final ClientDetailsService clientDetailsService;
    private final PhoneNumberService phoneNumberService;

    public String register(RegisterRequest registerRequest) {
        String[] fio = registerRequest.getFullName().split(" ");
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        String phoneString = registerRequest.getPhoneNumber();

        Client client = clientService.createNewClient(fio);
        PhoneNumber phoneNumber = phoneNumberService.createNewPhoneNumber(client, phoneString);
        ClientDetails clientDetails = clientDetailsService.createNewClientDetails(client, encodedPassword);
        client.setPhoneNumber(phoneNumber);
        client.setClientDetails(clientDetails);

        client = clientService.saveClient(client);
        User user = User.builder()
                .client(client)
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
        return jwtService.generateToken(user);
    }

    public String authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getPhoneNumber(),
                        authRequest.getPassword()
                )
        );
        User user = userRepository.findByPhoneNumber(authRequest.getPhoneNumber()).orElseThrow();
        return jwtService.generateToken(user);
    }
}
