package com.tamaspinter.fileretrievalservice.exception;

public class FileNotFoundException extends Exception {
    private static final String FILE_NOT_FOUND_MESSAGE = "Could not find any file matching the search criteria";

    public FileNotFoundException() {
        super(FILE_NOT_FOUND_MESSAGE);
    }
}
