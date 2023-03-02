package com.trungdq.springbatch.listener;

import com.trungdq.springbatch.model.StudentCsv;
import com.trungdq.springbatch.model.StudentJson;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Component
public class SkipListenerImpl implements SkipListener<StudentCsv, StudentJson> {
    @Override
    public void onSkipInRead(Throwable throwable) {
        if (throwable instanceof FlatFileParseException) {
            createFile("D:\\MyStack\\spring-batch-example\\Chunk Job\\Chunk Step\\reader\\SkipInRead.txt",
                    ((FlatFileParseException) throwable).getInput());
        }
    }

    @Override
    public void onSkipInWrite(StudentJson studentJson, Throwable throwable) {
        createFile("D:\\MyStack\\spring-batch-example\\Chunk Job\\Chunk Step\\processor\\SkipInProcess.txt",
                studentJson.toString());
    }

    @Override
    public void onSkipInProcess(StudentCsv studentCsv, Throwable throwable) {
        createFile("D:\\MyStack\\spring-batch-example\\Chunk Job\\Chunk Step\\writer\\SkipInWrite.txt",
                studentCsv.toString());
    }

    private void createFile(String filePath, String data) {
        try (FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
            fileWriter.write(data + " " + new Date() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
