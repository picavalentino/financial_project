import com.team.financial_project.customer.Enum.CustomerStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerStatusTypeHandler extends BaseTypeHandler<CustomerStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CustomerStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public CustomerStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return CustomerStatus.fromValue(value);
    }

    @Override
    public CustomerStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        return CustomerStatus.fromValue(value);
    }

    @Override
    public CustomerStatus getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
        int value = cs.getInt(columnIndex);
        return CustomerStatus.fromValue(value);
    }
}
