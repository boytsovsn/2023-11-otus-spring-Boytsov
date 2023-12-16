package ru.otus.hw.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/
        FileReader fileReader = null;
        CsvToBean<QuestionDto>  csvBean = null;
        try {
            fileReader = new FileReader(fileNameProvider.getTestFileName());
            csvBean = new CsvToBeanBuilder(fileReader).withType(QuestionDto.class)
                    .withSeparator(';').withSkipLines(1).build();
        } catch (FileNotFoundException fe) {
            throw new QuestionReadException("File not found, filename - " + fileNameProvider.getTestFileName(), fe);
        } catch (IOException ie) {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException ce) {
                throw new QuestionReadException("Close questions file error, filename - " + fileNameProvider.getTestFileName(), ce);
            }
            throw new QuestionReadException("Read questions error, filename - " + fileNameProvider.getTestFileName(), ie);
        }

        return csvBean != null ? csvBean.stream().map(x->{return x.toDomainObject();}).toList() : new ArrayList<>();
    }
}
