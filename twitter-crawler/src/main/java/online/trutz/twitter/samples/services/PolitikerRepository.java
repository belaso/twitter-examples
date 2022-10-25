package online.trutz.twitter.samples.services;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import online.trutz.twitter.samples.model.Politiker;

@Repository
public interface PolitikerRepository extends MongoRepository<Politiker, String> {

}
