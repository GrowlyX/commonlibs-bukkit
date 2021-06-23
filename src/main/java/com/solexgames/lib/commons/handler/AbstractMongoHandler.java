package com.solexgames.lib.commons.handler;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

/**
 * @author GrowlyX
 * @since 6/22/2021
 */

@Getter
@Setter
public class AbstractMongoHandler {

    private MongoClient client;
    private MongoDatabase database;

    private MongoCollection<Document> playerCollection;

//    public AbstractMongoHandler() {
//        this.client = CorePlugin.getInstance().getCoreDatabase().getClient();
//
//        this.database = this.client.getDatabase("admin");
//        this.playerCollection = this.database.getCollection("Friends");
//    }

}
