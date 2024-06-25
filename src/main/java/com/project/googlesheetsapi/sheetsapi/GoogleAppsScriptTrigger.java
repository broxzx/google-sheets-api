package com.project.googlesheetsapi.sheetsapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.*;
import lombok.SneakyThrows;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class GoogleAppsScriptTrigger {

    private static final String APPLICATION_NAME = "Google Apps Script Trigger Example";
    private static final String SCRIPT_CONTENT = "function myFunction() {\n" +
            "  // Your code here\n" +
            "  Logger.log('This function runs every 5 minutes');\n" +
            "}\n" +
            "\n" +
            "function createTrigger() {\n" +
            "  var triggers = ScriptApp.getProjectTriggers();\n" +
            "  for (var i = 0; i < triggers.length; i++) {\n" +
            "    ScriptApp.deleteTrigger(triggers[i]);\n" +
            "  }\n" +
            "  ScriptApp.newTrigger('myFunction')\n" +
            "           .timeBased()\n" +
            "           .everyMinutes(5)\n" +
            "           .create();\n" +
            "}";

    @SneakyThrows
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        // Авторизация
        Credential credential = GoogleScriptService.getCredentials();

        // Создание клиента для доступа к Google Apps Script API
        Script service = new Script.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Создание проекта Google Apps Script
        CreateProjectRequest createProjectRequest = new CreateProjectRequest().setTitle("My Script Project");
        Project project = service.projects().create(createProjectRequest).execute();
        String scriptId = project.getScriptId();

        File manifestFile = new File()
                .setName("appsscript")
                .setType("JSON")
                .setSource("{\"timeZone\":\"America/New_York\",\"exceptionLogging\":\"STACKDRIVER\"}");

        // Добавление кода в проект
        service.projects().updateContent(scriptId, new Content()
                .setFiles(Arrays.asList(new File()
                        .setName("Code")
                        .setType("SERVER_JS")
                        .setSource(SCRIPT_CONTENT), manifestFile))
                .setScriptId(scriptId)).execute();

        // Выполнение функции createTrigger для создания триггера
        ExecutionRequest request = new ExecutionRequest().setFunction("createTrigger");
        Operation op;
        do {
            op = service.scripts().run(scriptId, request).execute();
            if (op.getError() != null) {
                System.out.println("Error message: " + op.getError().getMessage());
                if (op.getError().getMessage().contains("Authorization")) {
                    System.out.println("Authorization required. Please authorize the script.");
                    Thread.sleep(5000); // Wait for authorization
                }
            }
        } while (op.getError() != null && op.getError().getMessage().contains("Authorization"));

        if (op.getError() == null) {
            System.out.println("Trigger created successfully");
        }
    }
}
