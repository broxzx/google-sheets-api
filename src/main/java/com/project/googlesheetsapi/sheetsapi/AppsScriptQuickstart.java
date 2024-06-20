//package com.project.googlesheetsapi.sheetsapi;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.script.Script;
//import com.google.api.services.script.model.*;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.security.GeneralSecurityException;
//import java.util.Arrays;
//import java.util.List;
//
//public class AppsScriptQuickstart {
//    private static final String APPLICATION_NAME = "Apps Script API Java Quickstart";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    private static final String TOKENS_DIRECTORY_PATH = "tokens";
//    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
//
//    private static final List<String> SCOPES =
//            List.of("https://www.googleapis.com/auth/script.projects", "https://www.googleapis.com/auth/script.deployments");
//
//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
//            throws IOException {
//        InputStream in = AppsScriptQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        if (in == null) {
//            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
//        }
//
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//    }
//
//    public static void main(String... args) throws IOException, GeneralSecurityException {
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        Script service = new Script.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//        Script.Projects projects = service.projects();
//
//        // Creates a new script project.
//        Project createOp = projects.create(new CreateProjectRequest().setTitle("My Script")).execute();
//
//        // Uploads two files to the project.
//        File file1 = new File()
//                .setName("hello")
//                .setType("SERVER_JS")
//                .setSource("function onEdit(e) {\n" +
//                        "  console.log(\"Cell edited: \" + e.range.getA1Notation());\n" +
//                        "}\n");
//        File file2 = new File()
//                .setName("appsscript")
//                .setType("JSON")
//                .setSource("{\"timeZone\":\"America/New_York\",\"exceptionLogging\":\"CLOUD\"}");
//        Content content = new Content().setFiles(Arrays.asList(file1, file2));
//        Content updatedContent = projects.updateContent(createOp.getScriptId(), content).execute();
//
//        // Deploy the script project with specific version
//        DeploymentConfig deploymentConfig = new DeploymentConfig()
//                .setVersionNumber(1); // Specify the version number here
//
//        service.projects().deployments().update(createOp.getScriptId(), "1", new UpdateDeploymentRequest().setDeploymentConfig(deploymentConfig)).execute();
//
//        // Logs the project URL.
//        System.out.printf("https://script.google.com/d/%s/edit\n", updatedContent.getScriptId());
//    }
//}
