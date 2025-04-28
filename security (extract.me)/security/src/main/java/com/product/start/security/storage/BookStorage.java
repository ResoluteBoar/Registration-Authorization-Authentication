package com.product.start.security.storage;

import com.product.start.security.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class BookStorage {

    private Map<String, Book> bookMap;

    @Autowired
    public BookStorage(Map<String,Book> bookMap) {
        this.bookMap = bookMap;
    }

    public Book findBookByName(String name){ return bookMap.get(name); }

    public void addBook(Book book){
        bookMap.put(book.getName(), book);
    }
}
