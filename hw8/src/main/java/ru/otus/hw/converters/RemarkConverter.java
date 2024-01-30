package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Remark;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RemarkConverter {
    public String remarksToString(List<Remark> remarks) {
        return remarks.stream().map(this::remarkToString).collect(Collectors.joining(", "));
    }

    public String remarkToString(Remark remark) {
        return "[Id: %s, Remark: %s]".formatted(remark.getId(), remark.getRemarkText());
    }
}
