package com.gomezrondon.search;

import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;

import static com.gomezrondon.search.IndexerKt.dontSearchList;
import static com.gomezrondon.search.IndexerKt.generateIndexFile_2;
import static com.gomezrondon.search.IndexerKt.getFolderName;
import static com.gomezrondon.search.IndexerKt.loadFolders;

public class Indexer {

    public static void main(String[] args)  {

        var folders = loadFolders();

        folders.parallelStream().forEach(folder ->{
            indexexReactive(folder);
        });

        System.out.printf(">>>>>>>>");
    }

    public static void indexexReactive(String folder) {
        String folderName = getFolderName(folder);
        var f_name = "repository" + File.separator + "output_" + folderName + ".txt";


        try {
        List<List<String>> block = readFile(f_name)
                .skip(3)
                .filter(x -> x.length() > 0)
                .skipLast(4)
                .windowWhile(x -> !x.startsWith("   "))
                .flatMap(x -> x.map(String::trim).collectList())
                .collectList().block();

               // generateIndexFile(folder, Objects.requireNonNull(block)); // procesamos
                generateIndexFile_2(folder, Objects.requireNonNull(block));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Flux<String> fromPath(Path path) {
        return Flux.using(() -> Files.lines(path, Charset.forName("Cp1250")),
                Flux::fromStream,
                BaseStream::close
        );
    }

    public static Flux<String> readFile(String file) throws IOException {

        if(file != null){
            Flux<String> stringFlux = fromPath(Paths.get(file));
            return stringFlux;
        }

        return null;
    }

}


