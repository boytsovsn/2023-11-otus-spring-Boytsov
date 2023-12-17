package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
@NoArgsConstructor
public class QuestionReaderImpl implements QuestionReader {

    public List<QuestionDto> readCsvQuestions(String questionsFileName) {
        FileReader fileReader = null;
        CsvToBean<QuestionDto> csvBean = null;
        try {
            File file = new ClassPathResource(questionsFileName).getFile();
            fileReader = new FileReader(file);
            csvBean = new CsvToBeanBuilder(fileReader).withType(QuestionDto.class)
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
