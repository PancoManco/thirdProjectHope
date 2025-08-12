package utils;

import java.sql.SQLException;

public final class UniqueConstantValidator {
    private static final String DUPLICATE_ERROR = "23505";
    private UniqueConstantValidator() {
    }
    public static boolean isUniqueConstant(SQLException e) {
        return DUPLICATE_ERROR.equals(e.getSQLState());
    }
}
