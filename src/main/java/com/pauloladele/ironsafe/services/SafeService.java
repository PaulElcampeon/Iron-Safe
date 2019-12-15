package com.pauloladele.ironsafe.services;

import com.mongodb.client.result.UpdateResult;
import com.pauloladele.ironsafe.dto.AddCredentialsRequest;
import com.pauloladele.ironsafe.dto.RemoveCredentialsRequest;
import com.pauloladele.ironsafe.models.Credential;
import com.pauloladele.ironsafe.models.Safe;
import com.pauloladele.ironsafe.repositories.SafeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SafeService implements SafeInterface {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SafeRepository repository;

    @Override
    public void createSafe(String email) {
        repository.insert(new Safe(email));
    }

    @Override
    public Safe getSafe(String email) {
        return repository.findById(email).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public boolean addCredentials(AddCredentialsRequest addCredentialsMessage, String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(email));

        Update update = new Update();
        update.addToSet("credentials", new Credential(addCredentialsMessage.getKey(), addCredentialsMessage.getValue()));

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Safe.class);

        return updateResult.getModifiedCount() == 1;
    }

    @Override
    public boolean removeCredentials(RemoveCredentialsRequest removeCredentialsMessage, String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(email)
                .and("credentials.key").is(removeCredentialsMessage.getKey()));

        Update update = new Update();
        update.pull("credentials", new Credential(removeCredentialsMessage.getKey(), removeCredentialsMessage.getValue()));

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Safe.class);

        return updateResult.getModifiedCount() == 1;
    }
}
