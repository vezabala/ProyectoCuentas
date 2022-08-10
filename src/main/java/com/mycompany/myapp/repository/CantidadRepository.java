package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cantidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cantidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CantidadRepository extends JpaRepository<Cantidad, Long> {}
