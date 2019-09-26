package com.gomezrondon.search

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.MongoClient
import com.mongodb.MongoCredential
import com.mongodb.client.FindIterable
import org.bson.Document
import com.mongodb.client.model.Filters.eq

//https://www.tutorialspoint.com/mongodb/mongodb_java.htm
// http://zetcode.com/java/mongodb/  (como borrar)
class MongoDBConnection {

    private val mongo: MongoClient

    // Accessing the database
    val database: MongoDatabase
        get() = mongo.getDatabase("testDb")

    init {
        // Creating a Mongo client
        mongo = MongoClient("localhost", 27017)

        // Creating Credentials
        val credential: MongoCredential
        credential = MongoCredential.createCredential("sampleUser", "testDb", "password".toCharArray())
        println("Connected to the database successfully")
        println("Credentials ::$credential")
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val conn = MongoDBConnection()
            val database = conn.database
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

/*            //--------------------- testing with real data
            val collection = database.getCollection("documentx")
            var list = mutableListOf<String>()
            list.add("empty,image,name,pid,session,name,session,mem,usage,system,idle,process,services,system,services,424,secure,system,services")
            list.add("3080,services,432,svchost,exe,3140,services,780,svchost,exe,3192,services,628,svchost,exe,3240,services,724,svchost,exe")
            list.add("vmms,exe,4976,services,156,svchost,exe,5012,services,476,svchost,exe,5140,services,692,svchost,exe,5380,services")

            val dataFile = DataFile("e4eabfa613cbc5cf8bb20146dc1ea9fd", "data-file", "C:\\temp\\salida.txt", list)
            collection.insertOne(dataFile.getMongoDocument())*/

            //-------------------------- how to update a document a especific field
            /*
        // esto funciona
        collection.updateOne(new Document("title", "dat-file"), new Document("$set", new Document("text", list)));
*/

            //--------------------------- find one and update if exist

            val collection = database.getCollection("documentx")

            val dataFile = DataFile(id = "9193bebd983e5b9384a2491fcdceacaa",  lines = listOf<String>("Pepe"))

           // val iterDoc = collection.findOneAndUpdate(Document("doc_id", "9193bebd983e5b9384a2491fcdceacaa"), Document("\$set", Document("text", "A")))
            val iterDoc = collection.findOneAndUpdate(dataFile.getIdDocument(), dataFile.getReplaceLinesDocument())

            if (iterDoc == null ) {
                println("Inserting record: ")
            }else{
                println("iterDoc.size: ${iterDoc.size}")
                iterDoc.forEach { println(it) }
            }


        }
    }


    /* DEPRECADO
    public DB getDB() {
        // Accessing the database
        DB database = mongo.getDB("testDb");
        return database;
    }*/

}
