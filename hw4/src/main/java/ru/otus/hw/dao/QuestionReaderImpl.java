package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.mockito.internal.util.io.IOUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedMessagesService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

@Component
@RequiredArgsConstructor
public class QuestionReaderImpl implements QuestionReader {

    private final TestFileNameProvider fileNameProvider;
    private final LocalizedMessagesService localizedMessagesService;

    @Override
    public List<QuestionDto> readCsvQuestions() {
        CsvToBean<QuestionDto> csvBean = null;
        String questionsFileName = fileNameProvider.getTestFileName();
        if (questionsFileName == null)
                throw new QuestionReadException(localizedMessagesService.getMessage("QuestionsReader.Questions.filename.is.null"));
        try (InputStream iStream = this.getClass().getClassLoader().getResourceAsStream(questionsFileName)) {
            if (iStream == null)
                throw new QuestionReadException(localizedMessagesService.getMessage("QuestionsReader.Questions.input.stream.is.null"));
            byte[] buf = iStream.readAllBytes();
            Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf), StandardCharsets.UTF_8));
            csvBean = new CsvToBeanBuilder(reader).withType(QuestionDto.class)
                    .withSeparator(';').withSkipLines(1).build();
        } catch (FileNotFoundException fe) {
            throw new QuestionReadException(localizedMessagesService.getMessage("QuestionsReader.File.not.found.filename", questionsFileName), fe);
        } catch (IOException ie) {
            throw new QuestionReadException(localizedMessagesService.getMessage("QuestionsReader.Read.questions.error.filename", questionsFileName), ie);
        }
        return csvBean != null ? new ArrayList<QuestionDto>(csvBean.stream().toList()) : new ArrayList<QuestionDto>();
    }
}
