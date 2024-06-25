package com.project.googlesheetsapi.service;

import com.google.api.services.script.Script;
import com.google.api.services.script.model.Content;
import com.google.api.services.script.model.File;
import com.project.googlesheetsapi.sheetsapi.GoogleScriptService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class UpdateGoogleScript {

    private static final String SCRIPT_ID = "1i3QX7Ifc3qikPHMyxVmBNN8lteyjoG5KGlgv0i558wpDTfi-cyz_Ibyu";

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        Script service = GoogleScriptService.getScriptService();

        File codeFile = new File()
                .setName("Code")
                .setType("SERVER_JS")
                .setSource("function myFunction() { Logger.log('Hello, world!'); }");

        File manifestFile = new File()
                .setName("appsscript")
                .setType("JSON")
                .setSource("{\"timeZone\":\"America/Los_Angeles\",\"exceptionLogging\":\"STACKDRIVER\"}");

        Content content = new Content()
                .setFiles(Arrays.asList(codeFile, manifestFile));
        service.projects().updateContent(SCRIPT_ID, content).execute();

        System.out.println("Script updated successfully.");
    }
}
