package com.example.libraryJSP.repository;

import com.example.libraryJSP.entities.Book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface bookRepository extends JpaRepository<Book, Integer> {
	@Query("SELECT b FROM Book b WHERE b.id != :id AND b.title = :title AND b.author = :author")
	Book findByTitleAndAuthor(@Param("id") int id, @Param("title") String title, @Param("author") String author);
	
	List<Book> findByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase(String authorKeyword, String titleKeyword);
}
