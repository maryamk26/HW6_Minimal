package ru.itis.servlets;

import ru.itis.models.User;
import ru.itis.repository.AccountsRepository;
import ru.itis.repository.AccountsRepositoryJdbcImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


@WebServlet("/save")
public class SaveServlet extends HttpServlet {

    private AccountsRepository accountsRepository;

    private static final String DB_USER = "java";
    private static final String DB_PASSWORD = "1234";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/java3";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            accountsRepository = new AccountsRepositoryJdbcImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/html/save.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("first_name");
        String secondName = request.getParameter("second_name");
        int age = Integer.parseInt(request.getParameter("age"));
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = User.builder()
                .name(firstName)
                .surname(secondName)
                .age(age)
                .email(email)
                .password(password)
                .build();

        accountsRepository.save(user);
        response.sendRedirect("/login"); // Redirect to the login page
    }
}
