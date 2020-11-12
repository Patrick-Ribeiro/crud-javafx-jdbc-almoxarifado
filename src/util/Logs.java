package util;

import java.sql.PreparedStatement;

public class Logs {

    public static void informationQuery(PreparedStatement preparedStatement) {

    }

    public static void error(Exception exception) {
        System.err.println("ERROR -> " +exception.getClass().getSimpleName() +": "+ exception.getMessage());
    }
}
