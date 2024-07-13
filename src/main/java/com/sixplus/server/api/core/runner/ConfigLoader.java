package com.sixplus.server.api.core.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigLoader implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // ���� �� �ε� �۾�
        System.out.println("Loading CommandLineRunner application settings...");
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "] : " + args[i]);
        }
    }
}
