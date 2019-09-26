package com.gomezrondon.search;

import com.mongodb.DB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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

    private MongoDatabase getDatabase() {
        // Accessing the database
        MongoDatabase database = mongo.getDatabase("testDb");
        return database;
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

//--------------------- testing with real data
        MongoCollection<Document> collection = database.getCollection("documentx");
        List<String> list = new ArrayList();
        list.add("empty,image,name,pid,session,name,session,mem,usage,system,idle,process,services,system,services,424,secure,system,services");
        list.add("3080,services,432,svchost,exe,3140,services,780,svchost,exe,3192,services,628,svchost,exe,3240,services,724,svchost,exe");
        list.add("vmms,exe,4976,services,156,svchost,exe,5012,services,476,svchost,exe,5140,services,692,svchost,exe,5380,services");

        DataFile dataFile = new DataFile("e4eabfa613cbc5cf8bb20146dc1ea9fd", "data-file", "C:\\temp\\salida.txt", list);
        collection.insertOne(dataFile.getMongoDocument());

//-------------------------- how to update a document a especific field
/*
        // esto funciona
        collection.updateOne(new Document("title", "dat-file"), new Document("$set", new Document("text", list)));
*/

    }







    /* DEPRECADO
    public DB getDB() {
        // Accessing the database
        DB database = mongo.getDB("testDb");
        return database;
    }*/

}
