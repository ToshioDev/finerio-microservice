package me.alexisdev.fineriomicroservice.repositories;

import me.alexisdev.fineriomicroservice.models.Movement;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface MovementRepository extends MongoRepository<Movement,String>{

    Optional<Movement> findById(String id);
}
