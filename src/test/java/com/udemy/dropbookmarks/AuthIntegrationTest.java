package com.udemy.dropbookmarks;

import io.dropwizard.Application;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.*;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by apun.hiran on 3/14/18.
 */
public class AuthIntegrationTest {
    public static final String CONFIG_PATH =
            ResourceHelpers.resourceFilePath("test-config.yml");

    @ClassRule
    public static final DropwizardAppRule<DropBookmarksConfiguration> RULE
            = new DropwizardAppRule<>(DropBookmarksApplication.class, CONFIG_PATH);

    private static final HttpAuthenticationFeature FEATURE
            = HttpAuthenticationFeature.basic("udemy", "p@ssw0rd");

    private static final String TARGET =
            "http://localhost:9000";

    private static final String PATH =
            "/hello/secured";

    private static final String TARGETSSL =
            "https://localhost:9100";

    private static final String TRUST_STORE_FILE_NAME = "dropbookmarks.keystore";

    private static final String TRUST_STORE_PASSWORD = "p@ssw0rd";

    private javax.ws.rs.client.Client client;
    private javax.ws.rs.client.Client clientssl;

    @BeforeClass
    public static void setUpClass() throws Exception{
        Application<DropBookmarksConfiguration> application =
                RULE.getApplication();

        application.run("db", "migrate", "-i DEV", CONFIG_PATH);
    }

    @Before
    public void setUp() {
        SslConfigurator configurator
                = SslConfigurator.newInstance();
        configurator.trustStoreFile(TRUST_STORE_FILE_NAME)
                .trustStorePassword(TRUST_STORE_PASSWORD);
        SSLContext context = configurator.createSSLContext();

        client = ClientBuilder.newClient();
        clientssl = ClientBuilder.newBuilder()
                .sslContext(context)
                .build();
    }
    @After
    public void tearDown() {
        client.close();
        clientssl.close();
    }

    @Test
    public void testSadPath(){
        javax.ws.rs.core.Response response = client
                .target(TARGET)
                .path(PATH)
                .request()
                .get();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),response.getStatus());
    }

    @Test
    public void testSadPathSsl(){
        javax.ws.rs.core.Response response = clientssl
                .target(TARGET)
                .path(PATH)
                .request()
                .get();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),response.getStatus());
    }

    @Test
    public void testHappyPath(){
        String expected = "Hello Secured World!";
        client.register(FEATURE);
        String actual = client
                .target(TARGET)
                .path(PATH)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);

        assertEquals(expected,actual);

    }

    @Test
    public void testHappyPathSsl(){
        String expected = "Hello Secured World!";
        clientssl.register(FEATURE);
        String actual = clientssl
                .target(TARGETSSL)
                .path(PATH)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);

        assertEquals(expected,actual);

    }
}
