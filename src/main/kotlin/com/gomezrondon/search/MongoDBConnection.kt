package com.gomezrondon.search

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.MongoClient
import com.mongodb.MongoCredential
import com.mongodb.client.FindIterable
import org.bson.Document
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Filters
import com.mongodb.client.model.InsertOneModel
import com.mongodb.client.model.Sorts
import java.util.*
import java.util.ArrayList
import com.mongodb.client.model.WriteModel
import com.mongodb.client.model.BulkWriteOptions






//https://www.tutorialspoint.com/mongodb/mongodb_java.htm
// http://zetcode.com/java/mongodb/  (como borrar)
class MongoDBConnection {

    /* private val mongo: MongoClient



     // Accessing the database
     val database: MongoDatabase
         get() = mongo.getDatabase("testDb")

     init {
         // Creating a Mongo client
         mongo = MongoClient("192.168.99.100", 27017)

         // Creating Credentials
         val credential: MongoCredential
         credential = MongoCredential.createCredential("sampleUser", "testDb", "password".toCharArray())
         println("Connected to the database successfully")
         println("Credentials ::$credential")

     }*/

    companion object {
        var mongo: MongoClient = MongoClient("192.168.99.100", 27017)
        var iniOne: String? = null
        fun getMongoClientConnection():MongoClient {

            if (iniOne == null) {
                // Creating Credentials
                val credential: MongoCredential
                credential = MongoCredential.createCredential("sampleUser", "testDb", "password".toCharArray())
                println("Connected to the database successfully")
                println("Credentials ::$credential")
                iniOne = "done"
            }

          return mongo
        }

        fun getDataBase2():MongoDatabase {
            return getMongoClientConnection().getDatabase("testDb")
        }


        @JvmStatic
        fun main(args: Array<String>) {
           // val conn = MongoDBConnection()
            val database = getDataBase2()
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
        collection.updateOne(new Document("title", "dat-file"), new Document("$set", new Document("doc_texto", list)));
*/

            //--------------------------- find one and update if exist

/*            val collection = database.getCollection("documentx")

            val dataFile = DataFile(id = "9193bebd983e5b9384a2491fcdceacaa",  lines = listOf<String>("Pepe"))

           // val iterDoc = collection.findOneAndUpdate(Document("doc_id", "9193bebd983e5b9384a2491fcdceacaa"), Document("\$set", Document("doc_texto", "A")))
            val iterDoc = collection.findOneAndUpdate(dataFile.getIdDocument(), dataFile.getReplaceLinesDocument())

            if (iterDoc == null ) {
                println("Inserting record: ")
            }else{
                println("iterDoc.size: ${iterDoc.size}")
                iterDoc.forEach { println(it) }
            }*/

            //--------------------------- Search text -- Funciona OK
/*
            val collection = database.getCollection("documentx")
            //val iterDoc = collection.find(Document("\$text", Document("\$search", "javier")))
            val iterDoc = collection.find(Document("\$text", Document("\$search", "\"javier gomez\""))).limit(10) // funcion ojo son las comillas
            iterDoc.forEach { println(it) }
*/


            //--------------------------- find by a specific field
/*            val collection = database.getCollection("documentx")
            //val findIterable = collection.find(eq("path", "C:\\temp\\dockerfile\\service\\test\\test.txt"))
            val findIterable = collection.find(eq("type", "binary-file"))
            findIterable.forEach { println(it) }*/

            //---------------- Select All Documents in a Collection
           // https://docs.mongodb.com/manual/tutorial/query-documents/
/*
            val collection = database.getCollection("documentx")
            val findIterable = collection.find(Document()).limit(10)
            findIterable.forEach { println(it) }
*/
//------------------------- find with regex
/*
            val collection = database.getCollection("documentx")
            val findIterable = collection.find(regex("path",".*py$")).limit(10) //busqueda por extension del archivo
            findIterable.forEach { println(it) }
*/

            // -------------------- dellete all element
            //collection.deleteMany(Document())

            //--------------------- how to query and return only the 2 fields
/*            val collection = database.getCollection("documentx")
            val findIterable = collection.find().projection(Projections.include("path","doc_id")).limit(10)
            findIterable.forEach { println(it) }*/

            //--------------------- Search
           // Capture 2019-09-23
/*            val collection = database.getCollection("documentx")
           // val findIterable = collection.find(regex("path",".*public.*".toLowerCase())).projection(Projections.include("path","doc_id")).limit(10)
            val findIterable = collection.find(regex("path",".*pictures.*".toLowerCase())).projection(Projections.include("path","doc_id")).limit(10)
            findIterable.forEach { println(it) }*/

            //--------------------- find all indexes
/*            val collection = database.getCollection("documentx")
            val listIndexes = collection.listIndexes()

            listIndexes.forEach {
                for ((k,v) in it) {
                    if (k == "name" && v == "doc_texto_index") {
                        println("K: $k value: $v")
                    }
                }
            }*/
            //--------------------- do a simple search
            //https://mongodb.github.io/mongo-java-driver/3.6/driver/tutorials/text-search/
            val collection = database.getCollection("documentx")
/*            val matchCount = collection.count(Filters.text("javier gomez"))
            println("Text search matches: $matchCount")*/

            // ------------- do a bulk insert -- funcona
            //https://mongodb.github.io/mongo-java-driver/3.6/driver/tutorials/bulk-writes/
            val bulkWriteOptions = BulkWriteOptions().ordered(false)
            val documents = ArrayList<WriteModel<Document>>()
            for (i in 1..10) {
                val document = DataFile("e4eabfa613cbc5cf8bb20146dc1ea9fd", "test-borrar", "C:\\temp\\salida.txt", listOf("hola", "mundo")).getMongoDocument()
                documents.add(InsertOneModel(document))
            }
             collection.bulkWrite(documents, bulkWriteOptions)


        }
    }


    /* DEPRECADO
    public DB getDB() {
        // Accessing the database
        DB database = mongo.getDB("testDb");
        return database;
    }*/

}
