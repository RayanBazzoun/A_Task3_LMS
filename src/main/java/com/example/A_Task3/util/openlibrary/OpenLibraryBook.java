package com.example.A_Task3.util.openlibrary;


import lombok.Data;
import java.util.List;

@Data
public class OpenLibraryBook{
    private final String title;
    private final List<String> authors;
}