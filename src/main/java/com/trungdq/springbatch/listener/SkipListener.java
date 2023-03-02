package com.trungdq.springbatch.listener;

import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Component
public class SkipListener {

    @OnSkipInRead
    public void skipInRead(Throwable throwable) {
        if (throwable instanceof FlatFileParseException) {
            createFile("D:\\MyStack\\spring-batch-example\\Chunk Job\\Chunk Step\\reader\\SkipInRead.txt",
                    ((FlatFileParseException) throwable).getInput());
        }
    }

    private void createFile(String filePath, String data) {
        try (FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
            fileWriter.write(data + " " + new Date() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
