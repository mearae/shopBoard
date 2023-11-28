package com.example.demo.repository;

import com.example.demo.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<BoardFile, Long> {
    void deleteByBoard_id(Long id);
    List<BoardFile> findByBoard_id(Long id);
}
