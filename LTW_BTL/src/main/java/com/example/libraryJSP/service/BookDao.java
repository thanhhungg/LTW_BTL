package com.example.libraryJSP.service;
import com.example.libraryJSP.DAO.CartDAO;
import com.example.libraryJSP.entities.Book;
import com.example.libraryJSP.repository.bookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookDao {
    @Autowired
    private bookRepository bookdao;

    public List<Book> getListBook(){
        return bookdao.findAll();
    }
    public List<Book> sortBook(Sort sort){
        return bookdao.findAll(sort);
    }
    public Book updateBook(Book book){
        return bookdao.save(book);
    }
    public Book getBook(int id){
        return bookdao.findById(id).get();
    }
    public void deleteBook(int id){
        Book book = getBook(id);
        bookdao.delete(book);
    }
    
    public Book getBook1(int id,String title, String author){
        Book book = null;
        try{
        book = bookdao.findByTitleAndAuthor(id,title,author);
        }catch (Exception e){
            e.printStackTrace();
        }
        return book;
    }
    public int getQuantity(int id){
        int quantity=0;
        try{
        quantity = CartDAO.getQuantity(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return quantity;
    }
    public List<Book> searchBooks(String keyword) {
        return bookdao.findByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase(keyword,keyword);
    }
}
