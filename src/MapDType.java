import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class MapDType {
    private MapDType() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String getValue(int type, String colName, ResultSet rs) throws SQLException {
        String result = null;
        switch (type) {
            case -16:
            case 1:
            case 12:
                result = rs.getString(colName);
                break;
            case -7:
                result = Boolean.toString(rs.getBoolean(colName));
                break;
            case 2:
                result = rs.getBigDecimal(colName).toString();
                break;
            case -6:
                result = Byte.toString(rs.getByte(colName));
                break;
            case 5:
                result = Short.toString(rs.getShort(colName));
                break;
            case 4:
                result = Integer.toString(rs.getInt(colName));
                break;
            case -5:
                result = Long.toString(rs.getLong(colName));
                break;
            case 6:
            case 7:
                result = Float.toString(rs.getFloat(colName));
                break;
            case 8:
                result = Double.toString(rs.getDouble(colName));
                break;
            case 91:
                result = rs.getDate(colName).toString();
                break;
            case 92:
                result = rs.getTime(colName).toString();
                break;
            case 93:
                result = rs.getTimestamp(colName).toString();
                break;
            default:
                break;
        }
        return result;
    }

    public static void setValue(int type, int col, String val, PreparedStatement statement) throws SQLException {
        switch (type) {
            case -16:
            case 1:
            case 12:
                statement.setString(col, val);
                break;
            case -7:
                statement.setBoolean(col, Boolean.parseBoolean(val));
                break;
            case 2:
                statement.setBigDecimal(col, new BigDecimal(val));
                break;
            case -6:
                statement.setByte(col, Byte.parseByte(val));
                break;
            case 5:
                statement.setShort(col, Short.parseShort(val));
                break;
            case 4:
                statement.setInt(col, Integer.parseInt(val));
                break;
            case -5:
                statement.setLong(col, Long.parseLong(val));
                break;
            case 6:
            case 7:
                statement.setFloat(col, Float.parseFloat(val));
                break;
            case 8:
                statement.setDouble(col, Double.parseDouble(val));
                break;
            case 91:
                statement.setDate(col, Date.valueOf(val));
                break;
            case 92:
                statement.setTime(col, Time.valueOf(val));
                break;
            case 93:
                statement.setTimestamp(col, Timestamp.valueOf(val));
                break;
            default:
                break;
        }
    }
}
