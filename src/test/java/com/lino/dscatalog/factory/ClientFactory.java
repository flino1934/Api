package com.lino.dscatalog.factory;

import com.lino.dscatalog.dto.ClientDTO;
import com.lino.dscatalog.entities.Client;

import java.sql.Timestamp;
import java.time.Instant;

public class ClientFactory {

    public static Client createClient() {

        Client client = new Client(null, "Felipe Lino", "49366901808", Instant.parse("1997-06-05T10:00:00Z"), 0);
        return client;

    }

    public static ClientDTO createClientDTO() {

        Client client = createClient();
        return new ClientDTO(client);

    }

    public static Client createClientRepository() {
        Client client = new Client(1L, "Felipe Lino", "49366901808", Instant.parse("1997-06-05T10:00:00Z"), 0);
        return client;

    }

    public static ClientDTO createClientDTORepository() {

        Client Client = createClientRepository();
        return new ClientDTO(Client);

    }

}
