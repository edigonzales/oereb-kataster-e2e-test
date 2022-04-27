///usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS mavenCentral
//DEPS org.postgresql:postgresql:42.3.4 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class wait_for_database {

    public static void main(String... args) throws IOException, NumberFormatException, InterruptedException {
        String timeout = "10";
        String sleeptime = "5";
        String host = "localhost";
        String port = "5432";
        String database = null;
        String user = null;
        String password = null;
        String query = "select version()";


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
                System.err.println("-p       Host under test (default=localhost).");
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

//        jdbc:postgresql://server:5432/database

        String dburl = "jdbc:postgresql://"+host+":"+port+"/"+database;
        System.out.println(dburl);

        int timeoutInt = Integer.valueOf(timeout);
        int sleeptimeInt = Integer.valueOf(sleeptime);
        int i=0;
        while ((timeoutInt - i*sleeptimeInt) >= 0) {


            Thread.sleep(sleeptimeInt * 1000);

            System.out.println(timeoutInt - i*sleeptimeInt);

            i++;
        }

        // try (Connection conn = DriverManager.getConnection(dburl, "root", "password"); Statement stmt = conn.createStatement();) {
        //     ResultSet result = stmt.executeQuery(query);

        //     while (result.next()) System.out.println(result.getString(1));

        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
    }
}
