package ru.venidiktov;


import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.PrintWriter;

public class TestLauncher {

    public static void main(String[] args) {
        var launcher = LauncherFactory.create();
        var summaryGeneratingListener = new SummaryGeneratingListener();
//        launcher.registerLauncherDiscoveryListeners();
        launcher.registerTestExecutionListeners(summaryGeneratingListener);

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(DiscoverySelectors.selectPackage("ru.venidiktov.service"))
//                .listeners()
                .build();
        launcher.execute(request);

        try (PrintWriter printWriter = new PrintWriter(System.out)) {
            summaryGeneratingListener.getSummary().printTo(printWriter);
        }
    }
}
