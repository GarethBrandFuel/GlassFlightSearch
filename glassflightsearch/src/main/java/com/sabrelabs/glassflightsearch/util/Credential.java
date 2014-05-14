package com.sabrelabs.glassflightsearch.util;

import com.sabrelabs.glassflightsearch.MainActivity;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by barrettclark on 5/1/14.
 */
public class Credential {
    private final String TAG = getClass().getSimpleName();
    Properties properties;

    public Credential() throws IOException {
        properties = new Properties();
        InputStream input = null;
        String filename = "res/raw/credentials.properties";
        input = MainActivity.class.getClassLoader().getResourceAsStream(filename);
        properties.load(input);
    }

    public String getUser() { return properties.getProperty("user"); }
    public String getGroup() { return properties.getProperty("group"); }
    public String getDomain() { return properties.getProperty("domain"); }
    public String getPassword() { return properties.getProperty("password"); }
    public String getUri() { return properties.getProperty("uri"); }

    private String getClientID() {
        String str = "V1:" + this.getUser() + ":" + this.getGroup() + ":" + this.getDomain();
        byte[] encodedBytes = Base64.encodeBase64(str.getBytes());
        return new String(encodedBytes);
    }

    private String getClientSecret() {
        byte[] encodedBytes = Base64.encodeBase64(this.getPassword().getBytes());
        return new String(encodedBytes);
    }

    public String getEncodedCredentials() {
        String str = getClientID() + ":" + getClientSecret();
        byte[] encodedBytes = Base64.encodeBase64(str.getBytes());
        return new String(encodedBytes);
    }
}
