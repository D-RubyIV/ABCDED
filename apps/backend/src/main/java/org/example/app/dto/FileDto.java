package org.example.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FileDto {
    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "type is required")
    private String type;
    @NotNull(message = "data is required")
    private byte[] data;
    @NotNull(message = "download is required")
    private String download;
}
