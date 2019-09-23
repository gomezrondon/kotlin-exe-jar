package com.gomezrondon.search;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.BaseStream;

import static com.gomezrondon.search.IndexerKt.loadFolders;

public class test {

    public static void main(String[] args) throws InterruptedException {

        var folders = loadFolders();

        String fileName = "repository/output_temp.txt";

        Flux<String> stringFlux = null;
        try {
            stringFlux = readFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Mono<List<List<String>>> listMono = stringFlux
                .skip(3)
                .filter(x -> x.length() > 0)
                .skipLast(4)
                .windowWhile(x -> !x.startsWith("   "))
                .flatMap(x -> x.map(line -> line.trim()).collectList())
                .collectList();
        //.subscribe(x -> System.out.println("++++ "+x.toString()));

        List<List<String>> block = listMono.block();


        for (List<String> line : block) {
            System.out.println(line);
            System.out.println("-----------------------------");
        }
        System.out.printf(">>>>>>>>");
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


