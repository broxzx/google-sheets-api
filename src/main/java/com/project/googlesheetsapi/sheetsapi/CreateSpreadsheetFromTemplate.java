package com.project.googlesheetsapi.sheetsapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class CreateSpreadsheetFromTemplate {

    private static final String APPLICATION_NAME = "Google Sheets API Java";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String TEMPLATE_SPREADSHEET_ID = "1kaQ9F7YQtYHwKotNYv5kwsB6lCgAlGkti6EAp0Se9u4";

    private static final List<String> SCOPES =
            List.of(DriveScopes.DRIVE, SheetsScopes.SPREADSHEETS);


    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {

        InputStream in = CreateSpreadsheetFromTemplate.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Credential spreadSheetCredential = getCredentials(HTTP_TRANSPORT);

        Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        File copiedFile = new File();
        copiedFile.setName("Your Integration");
        File newFile = driveService.files().copy(TEMPLATE_SPREADSHEET_ID, copiedFile).execute();

        String newSpreadsheetId = newFile.getId();
        System.out.println("New Spreadsheet ID: " + newSpreadsheetId);

        Sheets sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, spreadSheetCredential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        ValueRange body = new ValueRange()
                .setValues(Collections.singletonList(Collections.singletonList("Hello, World!")));
        sheetsService.spreadsheets().values()
                .update(newSpreadsheetId, "integration!A2", body)
                .setValueInputOption("RAW")
                .execute();

        System.out.println("Spreadsheet created and updated successfully.");
        System.out.println("-".repeat(50));
        System.out.printf("Access your integration sheet: https://docs.google.com/spreadsheets/d/%s/edit?gid=0#gid=0%n", newSpreadsheetId);
    }
}
