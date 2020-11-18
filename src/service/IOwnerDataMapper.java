package service;

import domain.objects.Owner;

public interface IOwnerDataMapper {

    Owner read(String username);
    void update(String username, String password, String token);
    Owner readByToken(String token);
}
