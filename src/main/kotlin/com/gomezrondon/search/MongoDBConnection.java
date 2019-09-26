package com.gomezrondon.search;

import com.mongodb.DB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

//https://www.tutorialspoint.com/mongodb/mongodb_java.htm
// http://zetcode.com/java/mongodb/  (como borrar)
public class MongoDBConnection {

    private MongoClient mongo;

    public MongoDBConnection() {
        // Creating a Mongo client
        mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "testDb","password".toCharArray());
        System.out.println("Connected to the database successfully");
        System.out.println("Credentials ::"+ credential);
    }


    public static void main( String args[] ) {
        MongoDBConnection conn = new MongoDBConnection();
        MongoDatabase database = conn.getDatabase();
       // database.createCollection("documentx"); // solo se ejecuta 1 vez
        //System.out.println("Collection created successfully");

        //------------ como insertar
/*        Document document = new Document("title", "MongoDB")
                .append("id", 1)
                .append("description", "database")
                .append("likes", 100)
                .append("url", "http://www.tutorialspoint.com/mongodb/")
                .append("by", "tutorials point");

        MongoCollection<Document> collection = database.getCollection("documentx");
        collection.insertOne(document);
        */
 ///-------------------------------------- como leer
/*        // Getting the iterable object
        MongoCollection<Document> collection = database.getCollection("documentx");
        FindIterable<Document> iterDoc = collection.find();

        int i = 1;

        // Getting the iterator
        Iterator it = iterDoc.iterator();

        while (it.hasNext()) {
            System.out.println(">>> "+it.next());
            i++;
        }*/

//---------------------- borrar -- Funciona
/*        MongoCollection<Document> collection = database.getCollection("documentx");
        collection.deleteOne(eq("title", "MongoDB"));
        */

//------------------------------------------


    }

    public MongoDatabase getDatabase() {
        // Accessing the database
        MongoDatabase database = mongo.getDatabase("testDb");
        return database;
    }


    /* DEPRECADO
    public DB getDB() {
        // Accessing the database
        DB database = mongo.getDB("testDb");
        return database;
    }*/

}
