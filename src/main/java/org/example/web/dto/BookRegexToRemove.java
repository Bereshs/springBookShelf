package org.example.web.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
public class BookRegexToRemove {
    @NotEmpty(message = "wrong filed name")
    String field;
    @NotEmpty(message = "regex is empty")
    String regEx;

}
