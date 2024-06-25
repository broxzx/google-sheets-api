package com.project.googlesheetsapi.sheetsapi;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class GoogleAppsScriptAPI {
    private static final String CLIENT_SECRET_JSON = "src/main/resources/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/script.projects");
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static void main(String[] args) {
        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Reader clientSecretReader = new InputStreamReader(Files.newInputStream(Paths.get(CLIENT_SECRET_JSON)));
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);

            AuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                    .setAccessType("offline")
                    .build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).setCallbackPath("/Callback").build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

            String url = "https://script.googleapis.com/v1/projects";
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                request.addHeader("Authorization", "Bearer " + credential.getAccessToken());
                HttpResponse response = httpClient.execute(request);

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    System.out.println("Projects retrieved successfully!");
                } else {
                    System.err.println("Error: " + statusCode);
                    System.err.println("Response: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
