package org.example.app.repository;


import org.example.app.model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileModel, Long> {
    FileModel findByName(String name);
}
