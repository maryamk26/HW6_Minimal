package itis.mk.repository;

import itis.mk.models.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoRepositoryJdbcImpl implements UserInfoRepository {

    private final Connection connection;

    private static final String SQL_INSERT = "INSERT INTO user_info(first_name, last_name, age, email, password) VALUES ";

    public UserInfoRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(UserInfo user) {
        String sql = "INSERT INTO user_info (first_name, last_name, age, email, password) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Log the exception for debugging
            e.printStackTrace();
            // Optionally, you can rethrow the exception if needed
            throw new RuntimeException("Error saving user", e);
        }
    }

    @Override
    public UserInfo findByEmail(String email) {
        String sql = "SELECT * FROM user_info WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return UserInfo.builder()
                        .id(resultSet.getLong("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .age(resultSet.getInt("age"))
                        .email(resultSet.getString("email"))
                        .password(resultSet.getString("password"))
                        .build();
            }
        } catch (SQLException e) {
            // Log the exception for debugging
            e.printStackTrace();
            // Optionally, you can rethrow the exception if needed
            throw new RuntimeException("Error finding user by email", e);
        }
        return null; // Return null if the user is not found
    }
}
