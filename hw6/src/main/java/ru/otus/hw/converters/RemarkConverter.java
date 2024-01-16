package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Remark;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RemarkConverter {
    public String remarkToString(List<Remark> remarks) {
        return remarks.stream().map(x->{return "[Id: %d, Remark: %s]".formatted(x.getId(), x.getRemarkText()); }).collect(Collectors.joining(","));
    }
}
