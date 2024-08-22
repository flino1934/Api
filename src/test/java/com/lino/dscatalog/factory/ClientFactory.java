package com.lino.dscatalog.factory;

import com.lino.dscatalog.dto.ClientDTO;
import com.lino.dscatalog.entities.Client;

import java.time.Instant;

public class ClientFactory {

    public static Client createClient() {
        return new Client(null, "Felipe Lino", "49366901808", Instant.parse("2021-01-01T10:30:00Z"), 0);
    }

    public static ClientDTO createClientDTO() {
        Client client = createClient();
        return new ClientDTO(client);
    }

    // Método adicional para criar um ClientDTO com ID, se necessário para o teste
    public static ClientDTO createClientDTOWithId() {
        Client client = new Client(1L, "Felipe Lino", "49366901808", Instant.parse("2021-01-01T10:30:00Z"), 0);
        return new ClientDTO(client);
    }

}
