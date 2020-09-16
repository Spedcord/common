package xyz.spedcord.common.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoDBService {

    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MongoDBService(final String host, final int port, final String database) {
        final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        this.mongoClient = new MongoClient(host + ":" + port, MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        this.database = this.mongoClient.getDatabase(database);
    }

    public void stop() {
        this.mongoClient.close();
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

}
