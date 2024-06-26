package com.travel.domain.accommodation.repository;

import com.travel.domain.accommodation.entity.Accommodation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

  List<Accommodation> findByCategory(String category);
}
