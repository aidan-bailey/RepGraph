package com.repgraph.repgraph;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.repgraph" })
public class RepgraphApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepgraphApplication.class, args);
		File saveDir = new File("data/saved");
		if (!saveDir.exists()){
			saveDir.mkdirs();
		}
		File cacheDir = new File("data/cache");
		if (!cacheDir.exists()){
			cacheDir.mkdirs();
		}
	}

}