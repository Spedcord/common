package xyz.spedcord.common.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.PropertyCodecProvider;

import static org.bson.codecs.pojo.Conventions.DEFAULT_CONVENTIONS;

public class MongoDBService {

    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MongoDBService(final String host, final int port, final String database, PropertyCodecProvider... customProvider) {
        final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true)
                        .register(customProvider).conventions(DEFAULT_CONVENTIONS).build())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(new ConnectionString("mongodb://" + host + ":" + port))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        this.mongoClient = MongoClients.create(settings);
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
