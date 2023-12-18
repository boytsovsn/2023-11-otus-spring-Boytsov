package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionReaderImpl implements QuestionReader {

    private final TestFileNameProvider fileNameProvider;

    public List<QuestionDto> readCsvQuestions() {
        FileReader fileReader = null;
        CsvToBean<QuestionDto> csvBean = null;
        String questionsFileName = fileNameProvider.getTestFileName();
        if (questionsFileName == null)
                throw new QuestionReadException("Questions filename is null");
//        try (InputStream iStream = Application.class.getClassLoader().getResourceAsStream(questionsFileName)) {
        try {
            ClassPathResource classPathResource = null;
            if (questionsFileName != null)
                classPathResource = new ClassPathResource(questionsFileName);
            else
                throw new QuestionReadException("filename is null");
            File file = null;
            if (classPathResource != null)
                file = classPathResource.getFile();
            else
                throw new QuestionReadException("Path for file not found, filename - " + questionsFileName);
            Reader reader = new FileReader(file);
//            if (iStream == null)
//                throw new QuestionReadException("Questions input stream is null");
//            Reader reader = new BufferedReader(new InputStreamReader(iStream, StandardCharsets.UTF_8));
            csvBean = new CsvToBeanBuilder(reader).withType(QuestionDto.class)
                    .withSeparator(';').withSkipLines(1).build();
        } catch (FileNotFoundException fe) {
            throw new QuestionReadException("File not found, filename - " + questionsFileName, fe);
        } catch (IOException ie) {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException ce) {
                throw new QuestionReadException("Close questions file error, filename - " + questionsFileName, ce);
            }
            throw new QuestionReadException("Read questions error, filename - " + questionsFileName, ie);
        }
        return csvBean.stream().toList();
    }
}
