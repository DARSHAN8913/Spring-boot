package net.engineeringdigest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.engineeringdigest.entity.Plan;
import org.springframework.stereotype.Repository;

@Repository
public interface jmsrepo extends JpaRepository<Plan,Long> {

}
