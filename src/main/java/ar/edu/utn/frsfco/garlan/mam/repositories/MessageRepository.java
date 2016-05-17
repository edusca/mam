package ar.edu.utn.frsfco.garlan.mam.repositories;

import ar.edu.utn.frsfco.garlan.mam.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for manage the messages content with the database
 * 
 * <p><a href="MessageRepository.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
public interface MessageRepository extends MongoRepository<Message, String>{
    
}
