package fr.rosydev.handlers;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandLoader {

    public static void loadCommands(CommandHandler handler) {
        String folderPath = "commandes";
        Path folder = Paths.get(folderPath);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    String fileName = file.getFileName().toString();
                    if (fileName.endsWith(".class")) {
                        String className = fileName.replace(".class", "");
                        loadCommandClass(className, handler);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des commandes : " + e.getMessage());
        }
    }

    private static void loadCommandClass(String className, CommandHandler handler) {
        try {
            Class<?> commandClass = Class.forName(className);
            if (commandClass.isAnnotationPresent(Command.class)) {
                // Exécuter la commande correspondante
                executeCommand(handler, commandClass);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur lors du chargement de la classe de commande : " + e.getMessage());
        }
    }

    private static void executeCommand(CommandHandler handler, Class<?> commandClass) {
        try {
            Method method = commandClass.getMethod("execute");
            method.invoke(handler);  // Exécution de la commande dans le CommandHandler
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution de la commande : " + e.getMessage());
        }
    }

    // Autres méthodes du CommandLoader...
}
