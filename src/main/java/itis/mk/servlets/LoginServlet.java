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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String DB_USER = "java";
    private static final String DB_PASSWORD = "1234";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/java3";

    private AccountsRepository accountsRepository;

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
        request.getRequestDispatcher("/html/login.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = accountsRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            request.getSession().setAttribute("user", user);
            response.sendRedirect("/html/minimal.html");
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/html/login.html").forward(request, response);
        }
    }
}
