package ru.venidiktov.repo;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
public class BookRepoTest {

    @Container
    public static GenericContainer simpleWebServer
            = new GenericContainer("alpine:3.2")
            .withExposedPorts(80)
            .withCommand("/bin/sh", "-c", "while true; do echo "
                    + "\"HTTP/1.1 200 OK\n\nHello World!\" | nc -l -p 80; done");

    @Test
    public void givenSimpleWebServerContainer_whenGetReuqest_thenReturnsResponse()
            throws Exception {
        String address = "http://"
                + simpleWebServer.getContainerIpAddress()
                + ":" + simpleWebServer.getMappedPort(80);
        String response = simpleGetRequest(address);

        assertEquals(response, "Hello World!");
    }

    private String simpleGetRequest(String address) throws Exception {
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }
}