package com.sabrelabs.glassflightsearch.test;

import com.sabrelabs.glassflightsearch.util.Credential;

import junit.framework.TestCase;

import java.io.IOException;

/**
 * Created by barrettclark on 5/1/14.
 */
public class CredentialTest extends TestCase {
    Credential credential;
    protected void setUp() throws IOException {
        credential = new Credential();
    }

    public void testGetCredential() throws IOException {
        assertEquals("8987", credential.getUser());
        assertEquals("STPS", credential.getGroup());
        assertEquals("EXT", credential.getDomain());
        assertEquals("uR9gFO02", credential.getPassword());
        assertEquals("api.test.sabre.com", credential.getUri());
    }

    public void testGetCredentials() throws IOException {
        assertNotNull(credential.getEncodedCredentials());
    }
}
