package com.udemy.dropbookmarks.resources;

import com.udemy.dropbookmarks.core.User;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by apun.hiran on 3/2/18.
 */
public class HelloResourceTest {

    private static final HttpAuthenticationFeature FEATURE
            = HttpAuthenticationFeature.basic("u","p");

    private static final io.dropwizard.auth.Authenticator<BasicCredentials, User> AUTHENTICATOR
            = new io.dropwizard.auth.Authenticator<BasicCredentials, User>() {
        @Override
        public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException{
            return Optional.of(new User());
        }
    };

    @ClassRule
    public static final ResourceTestRule RULE
            = ResourceTestRule
            .builder()
            .addProvider(
                    new AuthDynamicFeature(
                            new BasicCredentialAuthFilter.Builder<User>()
                                    .setAuthenticator(AUTHENTICATOR)
                                    .setRealm("SECURITY REALM")
                                    .buildAuthFilter()
            ))
            .setTestContainerFactory(
                    new GrizzlyWebTestContainerFactory()
            )
            .addResource(new HelloResource())
            .build();

    @BeforeClass
    public static void setupClass() {
        RULE.getJerseyTest().client().register(FEATURE);

    }
    public HelloResourceTest() {
    }

    @Test
    public void testGetGreetings() throws Exception {
        String expectedResult = "Hello World!";
        String actualResult = RULE
                .getJerseyTest().target("/hello")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        assertEquals(expectedResult,actualResult);
    }

    @Test
    public void testGetSecuredGreetings() throws Exception {
        String expectedResult = "Hello Secured World!";
        String actualResult = RULE
                .getJerseyTest().target("/hello/secured")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        assertEquals(expectedResult,actualResult);

    }

}