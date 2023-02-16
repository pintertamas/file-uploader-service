package com.tamaspinter.fileuploadservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StorageProvider {
    Long id;
    String name;
    AuthType authType;
    String url;
}
