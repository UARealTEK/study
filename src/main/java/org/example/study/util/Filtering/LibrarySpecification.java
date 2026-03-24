package org.example.study.util.Filtering;

import org.example.study.DTOs.Entities.BookEntity;
import org.springframework.data.jpa.domain.Specification;

public class LibrarySpecification {

    public static Specification<BookEntity> byBookName(String name) {
        return (root, criteria, builder) ->
                builder.equal(root.get("name"), name);
    }

    public static Specification<BookEntity> byAuthor(String author) {
        return (root, criteria, builder) ->
                builder.equal(root.get("author"), author);
    }

    public static Specification<BookEntity> byAllFields(String name, String author) {
        Specification<BookEntity> spec = Specification.unrestricted();

        if (name != null) {
            spec = spec.and(byBookName(name));
        }
        
        if (author != null) {
            spec = spec.and(byAuthor(author));
        }

        return spec;
    }
}
