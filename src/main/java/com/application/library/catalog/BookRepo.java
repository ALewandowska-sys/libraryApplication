package com.application.library.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<BookModel, Long> {
    @Query("SELECT u FROM book u WHERE u.title LIKE %?1%")
    List<BookView> findByTitle(@Param(value = "title") String title);
    <T> List<T> findByAvailable(@Param(value = "available") boolean available, Class<T> type);
    List<BookModel> findByWhoBorrow(@Param(value = "whoBorrow") int id);
    BookModel findFirstById(Long id);
}
