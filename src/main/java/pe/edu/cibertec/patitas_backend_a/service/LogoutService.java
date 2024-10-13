package pe.edu.cibertec.patitas_backend_a.service;

import pe.edu.cibertec.patitas_backend_a.dto.LogoutRequestDTO;


public interface LogoutService {
    void logout(LogoutRequestDTO logoutRequestDTO) throws Exception;
}
