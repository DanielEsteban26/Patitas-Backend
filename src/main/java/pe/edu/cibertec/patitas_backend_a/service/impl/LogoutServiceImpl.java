package pe.edu.cibertec.patitas_backend_a.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.patitas_backend_a.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_backend_a.service.LogoutService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LogoutServiceImpl implements LogoutService {
    private static final String LOG_FILE_PATH = "src/main/java/pe/edu/cibertec/patitas_backend_a/logs/logout.log";
    private static final String USERS_FILE = "classpath:usuarios.txt";

    @Autowired
    private ResourceLoader resourceLoader;

    private Optional<String> getUserInfo(String tipoDocumento, String numeroDocumento) {
        try {
            Resource resource = resourceLoader.getResource(USERS_FILE);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                return br.lines()
                        .map(line -> line.split(";"))
                        .filter(parts -> parts.length >= 5 &&
                                parts[1].equals(tipoDocumento) &&
                                parts[2].equals(numeroDocumento))
                        .findFirst()
                        .map(parts -> String.join(", ", parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Lanza una excepción personalizada o maneja el error según tu lógica de negocio
            throw new RuntimeException("Error al leer el archivo de usuarios", e);
        }
    }

    private void logLogout(String tipoDocumento, String numeroDocumento) {
        File logFile = new File(LOG_FILE_PATH);
        logFile.getParentFile().mkdirs();  // Crea el directorio si no existe

        // Intenta obtener la información completa del usuario
        String userInfo = getUserInfo(tipoDocumento,numeroDocumento)
                .orElse(String.format("Tipo Documento: %s, Numero Documento: %s",
                        numeroDocumento, tipoDocumento));


        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(String.format("%s, Fecha: %s%n", userInfo, LocalDateTime.now()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al escribir en el archivo de log", e);
        }
    }

    @Override
    public void logout(LogoutRequestDTO logoutRequest) {
        logLogout(logoutRequest.tipoDocumento(), logoutRequest.numeroDocumento());
    }
}