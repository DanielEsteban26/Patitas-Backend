package pe.edu.cibertec.patitas_backend_a.service.impl;

import org.springframework.stereotype.Service;
import pe.edu.cibertec.patitas_backend_a.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_backend_a.service.LogoutService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class LogoutServiceImpl implements LogoutService {

    private static final String LOG_FILE_PATH = "src/main/java/pe/edu/cibertec/patitas_backend_a/logs/logout.log";

    @Override
    public LogoutRequestDTO logout(LogoutRequestDTO logoutRequest) {
        File logFile = new File(LOG_FILE_PATH);

        try (FileWriter writer = new FileWriter(logFile, StandardCharsets.UTF_8, true)) {
            LocalDateTime now = LocalDateTime.now();
            writer.write("Tipo Documento: " + logoutRequest.tipoDocumento() + ", Numero Documento: " + logoutRequest.numeroDocumento() + ", Fecha: " + now + "\n");
        } catch (IOException e) {
            System.out.println("Error de E/S: " + e.getMessage());
            throw new RuntimeException("Error: Error de E/S", e);
        }

        return logoutRequest;
    }

}