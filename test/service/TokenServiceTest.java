package service;

import datasource.datamapper.OwnerDataMapper;
import domain.objects.Owner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TokenServiceTest {

    private ITokenService tokenService;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private OwnerDataMapper ownerDataMapper;

    @Before
    public void initialize(){
        tokenService = new TokenService(ownerDataMapper);
    }

    @Test
    public void test_generateToken(){
        String token = tokenService.generateToken();
        Assert.assertEquals(10, token.length());
    }

    @Test
    public void test_validateToken_method(){
        Mockito.when(ownerDataMapper.readByToken("1234")).thenReturn(new Owner());
        boolean validation = tokenService.validateToken("1234");
        Assert.assertTrue(validation);
    }
}
