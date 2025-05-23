package com.example.A_Task3.util.openlibrary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class OpenLibraryClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenLibraryBook fetchBookByIsbn(String isbn) throws Exception {
        String url = String.format(
                "https://openlibrary.org/api/books?bibkeys=ISBN:%s&format=json&jscmd=data",
                isbn
        );
        String json = restTemplate.getForObject(url, String.class);

        JsonNode root = objectMapper.readTree(json);
        JsonNode bookNode = root.get("ISBN:" + isbn);
        if (bookNode == null || bookNode.isNull()) {
            throw new Exception("Book not found in Open Library for ISBN: " + isbn);
        }

        String title = bookNode.has("title") ? bookNode.get("title").asText() : null;
        if (title == null || title.isEmpty()) {
            throw new Exception("Title not found for ISBN: " + isbn);
        }


        List<String> authors = new ArrayList<>();
        if (bookNode.has("authors") && bookNode.get("authors").isArray()) {
            for (JsonNode authorNode : bookNode.get("authors")) {
                if (authorNode.has("name")) {
                    authors.add(authorNode.get("name").asText());
                }
            }
        }
        if (authors.isEmpty()) {
            throw new Exception("No authors found for ISBN: " + isbn);
        }

        return new OpenLibraryBook(title, authors);
    }
}