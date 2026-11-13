package edu.demo.voting;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Path dataDir = Paths.get(
            System.getProperty("user.home"),
            ".online-voting-system"
        );

        XmlStore.init(dataDir);
    }
}