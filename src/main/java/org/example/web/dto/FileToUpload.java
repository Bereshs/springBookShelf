package org.example.web.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FileToUpload {
    @NotEmpty
    String fileName;
}
