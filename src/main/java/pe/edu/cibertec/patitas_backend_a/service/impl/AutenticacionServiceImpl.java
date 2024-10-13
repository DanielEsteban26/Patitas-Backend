package pe.edu.cibertec.patitas_backend_a.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.patitas_backend_a.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_backend_a.service.AutenticacionService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AutenticacionServiceImpl implements AutenticacionService {

    @Autowired
    ResourceLoader resourceLoader;

    private Map<String, String[]> userStore = new HashMap<>();

    @Override
    public String[] validarUsuario(LoginRequestDTO loginRequestDTO) throws IOException {
        String[] datosUsuario = null;
        Resource resource = resourceLoader.getResource("classpath:usuarios.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (loginRequestDTO.tipoDocumento() != null && loginRequestDTO.tipoDocumento().equals(datos[0]) &&
                        loginRequestDTO.numeroDocumento() != null && loginRequestDTO.numeroDocumento().equals(datos[1]) &&
                        loginRequestDTO.password() != null && loginRequestDTO.password().equals(datos[2])) {

                    datosUsuario = new String[2];
                    datosUsuario[0] = datos[3]; // Recuperar nombre
                    datosUsuario[1] = datos[4]; // Recuperar correo

                    // Store user data in the map
                    String key = loginRequestDTO.tipoDocumento() + ":" + loginRequestDTO.numeroDocumento();
                    userStore.put(key, datosUsuario);

                    break;
                }
            }
        } catch (IOException e) {
            datosUsuario = null;
            throw new IOException(e);
        }

        return datosUsuario;
    }


}