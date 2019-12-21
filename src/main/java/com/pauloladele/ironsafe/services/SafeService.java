package com.pauloladele.ironsafe.services;

import com.mongodb.client.result.UpdateResult;
import com.pauloladele.ironsafe.dto.AddCredentialsRequest;
import com.pauloladele.ironsafe.dto.RemoveCredentialsRequest;
import com.pauloladele.ironsafe.models.Credential;
import com.pauloladele.ironsafe.models.Safe;
import com.pauloladele.ironsafe.repositories.SafeRepository;
import com.pauloladele.ironsafe.utils.StringEncrypterDecrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SafeService implements SafeInterface {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SafeRepository repository;

    @Autowired
    private StringEncrypterDecrypter encrypterDecrypter;

    @Override
    public void createSafe(String email) {
        repository.insert(new Safe(email));
    }

    @Override
    public Safe getSafe(String email) {
        Safe safe = repository.findById(email).orElseThrow(NoSuchElementException::new);

        List<String> credentials = safe
                .getCredentials()
                .stream()
                .map(cred -> encrypterDecrypter.decrypt(cred))
                .collect(Collectors.toList());

        safe.setCredentials(credentials);

        return safe;
    }

    @Override
    public boolean addCredentials(AddCredentialsRequest addCredentialsMessage, String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(email));

        String cred = addCredentialsMessage.getKey() +"."+ addCredentialsMessage.getValue();
        String encryptedCredential = encrypterDecrypter.encrypt(cred);

        Update update = new Update();
        update.addToSet("credentials", encryptedCredential);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Safe.class);

        return updateResult.getModifiedCount() == 1;
    }

    @Override
    public boolean removeCredentials(RemoveCredentialsRequest removeCredentialsMessage, String email) {
        Query query = new Query();
        String cred = removeCredentialsMessage.getKey() +"."+removeCredentialsMessage.getValue();
        String encryptedCredential = encrypterDecrypter.encrypt(cred);

        query.addCriteria(Criteria.where("_id").is(email));

        Update update = new Update();
        update.pull("credentials", encryptedCredential);

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Safe.class);

        return updateResult.getModifiedCount() == 1;
    }

//    @Override
//    public boolean removeCredentials(RemoveCredentialsRequest removeCredentialsMessage, String email) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("_id").is(email)
//                .and("credentials.key").is(removeCredentialsMessage.getKey()));
//
//        Update update = new Update();
//        update.pull("credentials", new Credential(removeCredentialsMessage.getKey(), removeCredentialsMessage.getValue()));
//
//        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Safe.class);
//
//        return updateResult.getModifiedCount() == 1;
//    }
}
