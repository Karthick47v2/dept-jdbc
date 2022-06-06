import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class MapDType {
    public static String getValue(int type, String colName, ResultSet rs) throws SQLException {
        String result = null;
        switch (type) {
            case -16:
            case 1:
            case 12:
                result = rs.getString(colName).toString();
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
        }
        return result;
    }

    public static void setValue(int type, String col, String val, boolean isUpdate, PreparedStatement statement,
            ResultSet rs) throws SQLException {
        switch (type) {
            case -16:
            case 1:
            case 12:
                if (isUpdate) {
                    rs.updateString(col, val);
                } else {
                    statement.setString(Integer.parseInt(col), val);
                }
                break;
            case -7:
                if (isUpdate) {
                    rs.updateBoolean(col, Boolean.parseBoolean(val));
                } else {
                    statement.setBoolean(Integer.parseInt(col), Boolean.parseBoolean(val));
                }
                break;
            case 2:
                if (isUpdate) {
                    rs.updateBigDecimal(col, new BigDecimal(val));
                } else {
                    statement.setBigDecimal(Integer.parseInt(col), new BigDecimal(val));
                }
                break;
            case -6:
                if (isUpdate) {
                    rs.updateByte(col, Byte.parseByte(val));
                } else {
                    statement.setByte(Integer.parseInt(col), Byte.parseByte(val));
                }
                break;
            case 5:
                if (isUpdate) {
                    rs.updateShort(col, Short.parseShort(val));
                } else {
                    statement.setShort(Integer.parseInt(col), Short.parseShort(val));
                }
                break;
            case 4:
                if (isUpdate) {
                    rs.updateInt(col, Integer.parseInt(val));
                } else {
                    statement.setInt(Integer.parseInt(col), Integer.parseInt(val));
                }
                break;
            case -5:
                if (isUpdate) {
                    rs.updateLong(col, Long.parseLong(val));
                } else {
                    statement.setLong(Integer.parseInt(col), Long.parseLong(val));
                }
                break;
            case 6:
            case 7:
                if (isUpdate) {
                    rs.updateFloat(col, Float.parseFloat(val));
                } else {
                    statement.setFloat(Integer.parseInt(col), Float.parseFloat(val));
                }
                break;
            case 8:
                if (isUpdate) {
                    rs.updateDouble(col, Double.parseDouble(val));
                } else {
                    statement.setDouble(Integer.parseInt(col), Double.parseDouble(val));
                }
                break;
            case 91:
                if (isUpdate) {
                    rs.updateDate(col, Date.valueOf(val));
                } else {
                    statement.setDate(Integer.parseInt(col), Date.valueOf(val));
                }
                break;
            case 92:
                if (isUpdate) {
                    rs.updateTime(col, Time.valueOf(val));
                } else {
                    statement.setTime(Integer.parseInt(col), Time.valueOf(val));
                }
                break;
            case 93:
                if (isUpdate) {
                    rs.updateTimestamp(col, Timestamp.valueOf(val));
                } else {
                    statement.setTimestamp(Integer.parseInt(col), Timestamp.valueOf(val));
                }
                break;
        }
    }
}
