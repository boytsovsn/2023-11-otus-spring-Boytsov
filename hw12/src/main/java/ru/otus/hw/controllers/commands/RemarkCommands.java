package ru.otus.hw.controllers.commands;

//import lombok.RequiredArgsConstructor;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import ru.otus.hw.converters.RemarkConverter;
//import ru.otus.hw.models.entities.Remark;
//import ru.otus.hw.services.RemarkService;

import java.util.Arrays;

//@RequiredArgsConstructor
//@ShellComponent
public class RemarkCommands {

//    private final RemarkService remarkService;
//
//    private final RemarkConverter remarkConverter;
//
//    @ShellMethod(value = "Find all remarks by book id", key = "rbid")
//    public String findAllRemarks(String id) {
//        return remarkConverter.remarksToString(remarkService.findByBookId(id));
//    }
//
//    @ShellMethod(value = "Find remark by id", key = "rid")
//    public String findRemarkById(String id) {
//        return remarkService.findById(id)
//                .map(remarkConverter::remarkToString)
//                .orElse("Remark with id %d not found".formatted(id));
//    }
//
//    // bins newRemark 1 1
//    @ShellMethod(value = "Insert remark", key = "rins")
//    public String insertRemark(String remarkText, String bookId) {
//        Remark savedRemark = remarkService.save("0", remarkText, bookId);
//        return remarkConverter.remarksToString(Arrays.asList(savedRemark));
//    }
//
//    // bupd 4 editedRemark 3 2
//    @ShellMethod(value = "Update remark", key = "rupd")
//    public String updateRemark(String id, String remarkText, String bookId) {
//        Remark savedRemark = remarkService.save(id, remarkText, bookId);
//        return remarkConverter.remarksToString(Arrays.asList(savedRemark));
//    }
//
//    // bdel 4
//    @ShellMethod(value = "Delete remark by id", key = "rdel")
//    public void deleteRemark(String id) {
//        remarkService.deleteById(id);
//    }
}
