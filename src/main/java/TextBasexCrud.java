import org.basex.*;
import org.basex.api.client.*;
import org.basex.core.*;
import org.basex.core.cmd.*;

import javax.xml.stream.*;
import java.io.*;
import java.util.Scanner;

public final class TextBasexCrud {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(final String[] args) throws Exception {
        Process serverProcess = startBaseXServer();

        System.out.println("=== BaseX CRUD Operations ===");
        // Configurar credenciales
        System.out.print("Enter username: ");
        String user = scanner.nextLine();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();

        // Iniciar servidor BaseX



        try {
            // Crear sesión cliente
            ClientSession session = new ClientSession("localhost", 1984, user, pass);

            // Seleccionar/crear base de datos
            System.out.print("Enter database name: ");
            String dbName = scanner.nextLine();
            manageDatabase(session, dbName);

            // Menú principal
            runCrudMenu(session, dbName);

            // Cerrar recursos
            session.close();
        } finally {
            stopBaseXServer(serverProcess);
        }
    }

    private static Process startBaseXServer() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "basexserver");
        builder.redirectErrorStream(true);
        return builder.start();
    }

    private static void stopBaseXServer(Process process) {
        try {
            new ProcessBuilder("cmd.exe", "/c", "basexserver stop").start();
            process.destroy();
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

    private static void manageDatabase(ClientSession session, String dbName) throws IOException {
        if (!checkDatabaseExists(session, dbName)) {
            createDatabase(session, dbName);
        }
        session.execute("OPEN " + dbName);
    }

    private static void runCrudMenu(ClientSession session, String dbName) throws IOException {
        String operation;
        do {
            System.out.println("\nSelect operation:");
            System.out.println("1. Create Record");
            System.out.println("2. Read Records");
            System.out.println("3. Update Record");
            System.out.println("4. Delete Record");
            System.out.println("5. Exit");
            System.out.print("Enter choice (1-5): ");
            operation = scanner.nextLine();

            switch (operation) {
                case "1":
                    createRecord(session, dbName);
                    break;
                case "2":
                    readRecords(session);
                    break;
                case "3":
                    updateRecord(session, dbName);
                    break;
                case "4":
                    deleteRecord(session, dbName);
                    break;
                case "5":
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!operation.equals("5"));
    }

    private static boolean checkDatabaseExists(ClientSession session, String dbName) {
        try {
            // Usar INFO DB para verificar existencia sin abrir la base de datos
            session.execute("INFO DB " + dbName);
            System.out.println("✅ Database '" + dbName + "' exists.");
            return true;
        } catch (IOException e) {
            // Manejar específicamente el error de "base de datos no encontrada"
            if (e.getMessage().contains("not found")) {
                System.out.println("❌ Database '" + dbName + "' not found.");
                return false;
            }
            System.out.println("⚠️ Error checking database: " + e.getMessage());
            return false;
        }
    }

    private static void createDatabase(ClientSession session, String dbName) throws IOException {
        System.out.println("Creating database '" + dbName + "'...");
        // XML de ejemplo: un contenedor "books" que almacenará documentos
        session.execute("CREATE DB " + dbName +" <root/>");
        System.out.println("Database '" + dbName + "' created successfully.");
    }

    private static void createRecord(ClientSession session, String dbName) {
        System.out.println("\nCreating a new record...");
        try {
            // Solicitar datos
            System.out.println("Enter XML to insert (e.g. <book><title>New Book</title></book>): ");
            String xml = scanner.nextLine();

            // Validar XML
            if (!isWellFormedXML(xml)) {
                System.out.println("Error: XML mal formado");
                return;
            }

            System.out.println("Enter the target XPath (e.g.: /books, //book[1]): ");
            String targetXPath = scanner.nextLine();

            // Construir comando XQuery correcto
            String command = String.format(
                    "XQUERY insert node %s into %s",
                    xml.replace("\"", "'"),  // Escapar comillas dobles
                    validateXPath(targetXPath)
            );

            // Ejecutar y confirmar cambios
            session.execute(command);


            System.out.println("Registro insertado exitosamente!");

        } catch (IOException e) {
            System.out.println("Error en inserción: " + e.getMessage());
        }
    }

    // Método auxiliar para validar XPath
    private static String validateXPath(String xpath) {
        // Asegurar que el XPath selecciona nodos existentes
        if (!xpath.startsWith("/") && !xpath.startsWith("//")) {
            return "/*[1]"; // Fallback a nodo raíz
        }
        return xpath;
    }


    private static void readRecords(ClientSession session) {
        System.out.println("\n--- Read Records ---");
        try {
            System.out.print("Enter XQuery: ");
            String query = scanner.nextLine();
            String result = session.execute("XQUERY " + query);
            System.out.println("Results:\n" + result);
        } catch (IOException e) {
            System.err.println("Error reading records: " + e.getMessage());
        }
    }

    private static void updateRecord(ClientSession session, String dbName) {
        System.out.println("\n--- Update Record ---");
        try {
            System.out.print("Enter the target XPath (e.g.: /books, //book[1]): ");
            String query = scanner.nextLine();
            System.out.print("Enter the new value (que sea sin comillas simples");
            String value = scanner.nextLine();


            session.execute("XQUERY replace value of node " + query +" with '"+value+"'");
            System.out.println("Record updated successfully.");
        } catch (IOException e) {
            System.err.println("Error updating record: " + e.getMessage());
        }
    }

    private static void deleteRecord(ClientSession session, String dbName) {
        System.out.println("\n--- Delete Record ---");
        try {
            System.out.print("Enter XQuery delete command: ");
            String query = scanner.nextLine();

            session.execute("XQUERY delete node " + query);
            System.out.println("Record deleted successfully.");
        } catch (IOException e) {
            System.err.println("Error deleting record: " + e.getMessage());
        }
    }

    private static boolean isWellFormedXML(String xml) {
        try {
            XMLInputFactory.newInstance()
                    .createXMLStreamReader(new StringReader(xml));
            return true;
        } catch (XMLStreamException e) {
            return false;
        }
    }
}