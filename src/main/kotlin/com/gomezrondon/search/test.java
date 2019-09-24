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

import static com.gomezrondon.search.IndexerKt.dontSearchList;
import static com.gomezrondon.search.IndexerKt.getFolderName;
import static com.gomezrondon.search.IndexerKt.loadFolders;

public class test {

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

                generateIndexFile(folder, Objects.requireNonNull(block)); // procesamos

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateIndexFile(String folder, List<List<String>> block) throws IOException {

        String folderName = getFolderName(folder);
        var f_name = "repository" + File.separator + "index_" + folderName + ".txt";

        Files.write(Paths.get(f_name), "".getBytes());

        for (List<String> line : block) {
            String regex = "Directory of ";
            String[] split = line.stream().findFirst().get().split(regex);
            String directory = split[1];

            var noSearchList = dontSearchList();
            boolean isBlackListed = !noSearchList.contains(SplitWorkLoadKt.getPathByLevel(6, directory));

            line.stream()
                    .filter(x -> isBlackListed)
                    .skip(1)
                    .map(x -> x.substring(39) +","+ x.substring(0,20))
                    .map(x -> x.split(","))
                    .map(arre -> arre[0] + "," + directory + File.separator + arre[0] + "," + arre[1]+"\n")
                    .forEach(linea -> {
                        try {
                            Files.write(Paths.get(f_name), linea.getBytes(), StandardOpenOption.APPEND);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

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


