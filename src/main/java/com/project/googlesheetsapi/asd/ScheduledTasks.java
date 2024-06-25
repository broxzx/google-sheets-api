package com.project.googlesheetsapi.asd;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private GoogleSheetsService googleSheetsService;

    @Scheduled(fixedRate = 18000)
    public void performTask() throws IOException, GeneralSecurityException {
        List<List<Object>> data = googleSheetsService.readData();
        data.forEach(System.out::println);

        List<List<Object>> newData = List.of(
                List.of("A", "B", "C", "D"),
                List.of("1", "2", "3", "4")
        );
        googleSheetsService.writeData(newData);
    }
}
