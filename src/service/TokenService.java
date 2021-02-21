package service;

import domain.objects.Owner;

import javax.inject.Inject;
import java.util.Random;

public class TokenService implements ITokenService {

    private IOwnerDataMapper ownerDataMapper;

    @Inject
    public TokenService(IOwnerDataMapper ownerDataMapper) {
        this.ownerDataMapper = ownerDataMapper;
    }

    @Override
    public String generateToken() {
        final int leftLimit = 97;
        final int rightLimit = 122;
        final int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    @Override
    public boolean validateToken(String token) {
        final Owner owner = ownerDataMapper.readByToken(token);
        return owner != null;
    }
}
