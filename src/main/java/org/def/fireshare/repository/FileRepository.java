package org.def.fireshare.repository;

import org.def.fireshare.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
