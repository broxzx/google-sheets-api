package com.project.googlesheetsapi.asd;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GoogleSheetsService {

    @Autowired
    private Sheets sheetsService;

    private static final String SPREADSHEET_ID = "1fQLyLoWqRwlzS58r1pbgiC99pGbjx7PuhR0iY0VDgAk";
    private static final String RANGE = "Sheet1!A1:D10";

    public List<List<Object>> readData() throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, RANGE)
                .execute();
        return response.getValues();
    }

    public void writeData(List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(values);
        sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, RANGE, body)
                .setValueInputOption("RAW")
                .execute();
    }
}
