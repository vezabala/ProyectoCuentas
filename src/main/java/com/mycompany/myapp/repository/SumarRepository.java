package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sumar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sumar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SumarRepository extends JpaRepository<Sumar, Long> {}
