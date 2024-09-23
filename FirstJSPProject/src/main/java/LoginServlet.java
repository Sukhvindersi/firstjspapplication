import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Path to the Excel file
    private static final String EXCEL_FILE_PATH = "C:/Users/sukhv/Desktop/credentialsWorkbook.xlsx";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (checkCredentials(username, password)) {
            // Redirect to dashboard if credentials match
            resp.sendRedirect("dashboard.html");
        } else {
            // Send error message if invalid
            req.setAttribute("errorMessage", "Invalid username or password!");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    private boolean checkCredentials(String username, String password) {
        try (FileInputStream fis = new FileInputStream(new File(EXCEL_FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the login credentials sheet
            Sheet sheet = workbook.getSheet("loginCredentials");

            // Loop through each row to find matching credentials
            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0);
                Cell passwordCell = row.getCell(1);

                // Skip the header row
                if (usernameCell.getRowIndex() == 0) continue;

                // Compare with input
                if (usernameCell.getStringCellValue().equals(username) && 
                    passwordCell.getStringCellValue().equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
