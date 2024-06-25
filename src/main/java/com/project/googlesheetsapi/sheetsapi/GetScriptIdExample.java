package com.project.googlesheetsapi.sheetsapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.ListVersionsResponse;
import com.google.api.services.script.model.Version;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GetScriptIdExample {
    private static final String APPLICATION_NAME = "Google Sheets Script Finder";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static Sheets sheetsService;
    private static Drive driveService;
    private static Script scriptService;

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        Credential credential = GoogleScriptService.getCredentials();

        sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        driveService = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        scriptService = new Script.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        String spreadsheetId = "1w1KYWh4RIRWZ1nHhtr7fESwqTwgY9ERAq1zL79_2Gro";
        findContainerBoundScripts(spreadsheetId);
    }

    private static void findContainerBoundScripts(String spreadsheetId) throws IOException {
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
        String title = spreadsheet.getProperties().getTitle();
        System.out.println("Spreadsheet Title: " + title);

        // Find Apps Script project files linked to the spreadsheet
        String query = "'" + spreadsheetId + "' in parents ";
        FileList result = driveService.files().list().setQ(query).setFields("files(id, name)").execute();
        List<File> files = result.getFiles();

        if (files == null || files.isEmpty()) {
            System.out.println("No container-bound scripts found.");
        } else {
            for (File file : files) {
                String scriptId = file.getId();
                System.out.printf("Found script project: %s (%s)\n", file.getName(), scriptId);

                ListVersionsResponse response = scriptService.projects().versions().list(scriptId).execute();
                List<Version> versions = response.getVersions();

                if (versions == null || versions.isEmpty()) {
                    System.out.println("No versions found for script: " + file.getName());
                } else {
                    for (Version version : versions) {
                        System.out.printf("Found script version: %s (%s)\n", version.getVersionNumber(), version.getDescription());
                    }
                }
            }
        }
    }
}
