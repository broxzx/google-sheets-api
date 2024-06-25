package com.project.googlesheetsapi.sheetsapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.DeveloperMetadata;
import com.google.api.services.sheets.v4.model.Spreadsheet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GetScript {
    private static final String APPLICATION_NAME = "Google Sheets Script Finder";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static Sheets sheetsService;

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        Credential credential = GoogleScriptService.getCredentials();

        sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        String spreadsheetId = "1w1KYWh4RIRWZ1nHhtr7fESwqTwgY9ERAq1zL79_2Gro";
        String projectId = getScriptProjectIdFromSheetProperties(spreadsheetId);
        System.out.println("Container-bound script project ID: " + projectId);
    }

    private static String getScriptProjectIdFromSheetProperties(String spreadsheetId) throws IOException {
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
        List<DeveloperMetadata> metadataList = spreadsheet.getDeveloperMetadata();

        for (DeveloperMetadata metadata : metadataList) {
            if ("scriptProjectId".equals(metadata.getMetadataValue())) {
                return metadata.getMetadataValue();
            }
        }

        return null;
    }
}
