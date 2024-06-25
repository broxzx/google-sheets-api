package com.project.googlesheetsapi.sheetsapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.script.Script;
import com.google.api.services.script.ScriptScopes;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleScriptService {
    private static final String APPLICATION_NAME = "Google Apps Script API";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CLIENT_SECRET_FILE_NAME = "src/main/resources/credentials.json";
    private static final List<String> SCOPES = List.of(ScriptScopes.SCRIPT_PROJECTS, DriveScopes.DRIVE, SheetsScopes.SPREADSHEETS);

    public static Credential getCredentials() throws IOException, GeneralSecurityException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new FileReader(CLIENT_SECRET_FILE_NAME));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
                .setExpirationTimeMilliseconds(1L);

        credential.refreshToken();

        return credential;
    }

    public static Script getScriptService() throws GeneralSecurityException, IOException {
        return new Script.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, getCredentials())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
