///usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS mavenCentral
//DEPS org.postgresql:postgresql:42.3.4 

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class wait_for_database {

    public static void main(String... args) throws IOException, NumberFormatException, InterruptedException {
        String timeout = "60";
        String sleeptime = "5";
        String host = "localhost";
        String port = "5432";
        String database = null;
        String user = null;
        String password = null;

        int argi = 0;
        for(;argi<args.length;argi++) {
            String arg = args[argi];
            
            if(arg.equals("-t")) {
                argi++;
                timeout = args[argi];
            } else if (arg.equals("-h")) {
                argi++;
                host = args[argi];
            } else if (arg.equals("-p")) {
                argi++;
                port = args[argi];
            } else if (arg.equals("-d")) {
                argi++;
                database = args[argi];
            } else if (arg.equals("-U")) {
                argi++;
                user = args[argi];
            } else if (arg.equals("-W")) {
                argi++;
                password = args[argi];
            } else if (arg.equals("--help")) {
                System.err.println();
                System.err.println("-t       Timeout in seconds (default=60).");
                System.err.println("-h       Host under test (default=localhost).");
                System.err.println("-p       Port under test (default=5432).");
                System.err.println("-d       Database name.");
                System.err.println("-U       Database username.");
                System.err.println("-W       Password");
                System.err.println();
                return;
            }
        }

        if (database == null) {
            System.err.println("Database required");
            System.exit(2);
        }

        if (user == null) {
            System.err.println("Username required");
            System.exit(2);
        }
        
        if (password == null) {
            System.err.println("Password required");
            System.exit(2);
        }

        String dburl = "jdbc:postgresql://"+host+":"+port+"/"+database;
        System.out.println("Database url: " + dburl);

        int timeoutInt = Integer.valueOf(timeout);
        int sleeptimeInt = Integer.valueOf(sleeptime);
        int i=0;
        boolean connected = false;
        while ((timeoutInt - i*sleeptimeInt) >= 0) {
            try (Connection conn = DriverManager.getConnection(dburl, user, password);) {
                System.out.println("Connection successful.");
                connected = true;
                break;
            } catch (SQLException e) {
                System.err.println((timeoutInt - i*sleeptimeInt) + "s: " + e.getMessage());
            }
            Thread.sleep(sleeptimeInt * 1000);
            i++;
        }
        if (connected == false) {
            System.exit(1);
        }
    }
}
