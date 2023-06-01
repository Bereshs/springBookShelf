package org.example.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BookIdToRemove {

    @NotNull(message = "id is empty")
    private Integer id;

}
