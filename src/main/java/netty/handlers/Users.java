package netty.handlers;

import netty.dbconnection.DbConn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Users {
    public List<UserEntity> usersList;
    private PreparedStatement ps;
    public Users()  {
        }


    public String getNick(String login, String password)  {
        String select = String.format("SELECT nick FROM users WHERE login = '%s' and password = '%s'",login, password);
        try{ps = DbConn.getInstance()
                .connection()
                .prepareStatement(select);
        ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String s = resultSet.getString(1);
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addNick(String nick, String login, String password)  {
       try {
           ps = DbConn.getInstance()
                   .connection()
                   .prepareStatement("INSERT INTO users (nick, login, password) VALUES (?,?,?)");
           ps.setString(1, nick);
           ps.setString(2, login);
           ps.setString(3, password);
           ps.executeUpdate();
           ps.close();
       } catch (SQLException s){

       }
    }
    public class UserEntity {
        private String login;
        private String password;
        private String rooth;

        public void setLogin(String login) {
            this.login = login;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setRooth(String rooth) {
            this.rooth = rooth;
        }

    }
}
