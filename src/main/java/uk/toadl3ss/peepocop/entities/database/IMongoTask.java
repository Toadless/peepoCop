package uk.toadl3ss.peepocop.entities.database;

import com.mongodb.client.MongoDatabase;

public interface IMongoTask
{
    /**
     *
     * @param database The {@link uk.toadl3ss.peepocop.entities.database.DatabaseManager database} to use.
     */
    public void run(MongoDatabase database);
}