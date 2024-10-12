package pe.edu.cibertec.patitas_backend_a.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.patitas_backend_a.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_backend_a.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_backend_a.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_backend_a.dto.LogoutResponseDTO;
import pe.edu.cibertec.patitas_backend_a.service.AutenticacionService;
import pe.edu.cibertec.patitas_backend_a.service.impl.LogoutServiceImpl;

import java.time.Duration;
import java.util.Arrays;

@RestController
@RequestMapping("/autenticacion")
public class AutenticacionController {

    @Autowired
    AutenticacionService autenticacionService;

    @Autowired
    private LogoutServiceImpl logoutService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {

        try {
            Thread.sleep(Duration.ofSeconds(1));
            String[] datosUsuario = autenticacionService.validarUsuario(loginRequestDTO);
            System.out.println("Resultado: " + Arrays.toString(datosUsuario));
            if (datosUsuario == null) {
                return new LoginResponseDTO("01", "Error: Usuario no encontrado", "", "");
            }
            return new LoginResponseDTO("00", "", datosUsuario[0], datosUsuario[1]);

        } catch (Exception e) {
            return new LoginResponseDTO("99", "Error: Ocurrió un problema", "", "");
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        try {

            logoutService.logout(logoutRequestDTO);

            return ResponseEntity.ok(new LogoutResponseDTO("00", "Cierre de Sesión exitoso",logoutRequestDTO.tipoDocumento(), logoutRequestDTO.numeroDocumento()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new LogoutResponseDTO("01", "Error: " + e.getMessage(), "", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new LogoutResponseDTO("99", "Error: Ocurrió un problema en el cierre de sesión", "", ""));
        }
    }
}
