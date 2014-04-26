package com.iansails;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * User: ian
 * Date: 11/23/13
 */
public class Responses {

    public static ResponseEntity<byte[]> file(InputStream is) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", URLConnection.guessContentTypeFromStream(is));
        return new ResponseEntity<byte[]>(IOUtils.toByteArray(is), headers, HttpStatus.OK);
    }

    public static ResponseEntity<String> jsonOk(String response) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    public static ResponseEntity<String> notLoggedIn() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        return new ResponseEntity<String>("You must be logged in to do that!", headers, HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<String> badrequest() {
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<String> badrequest(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        return new ResponseEntity<String>(message, headers, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<String> created() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    public static ResponseEntity<String> created(String response) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(response, headers, HttpStatus.CREATED);
    }

}