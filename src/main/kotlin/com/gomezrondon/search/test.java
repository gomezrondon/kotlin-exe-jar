package com.gomezrondon.search;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.BaseStream;

public class test {

    public static void main(String[] args) {
        Flux<String> stringFlux = null;
        try {
            stringFlux = readFile("repository/output_temp.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        stringFlux
                .skip(3)
                .filter(x -> x.length() > 0)
                .skipLast(5)
                .windowWhile(x -> !x.startsWith("  "))
                .map(x -> x.map(line -> line.trim()).subscribe(System.out::println))
                .subscribe(x -> System.out.println(">>>>>>>"));
        System.out.printf(">>>>>>>>");
    }

    private static Flux<String> fromPath(Path path) {
        return Flux.using(() -> Files.lines(path),
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


