package agenda.security.dto;

import javax.validation.constraints.NotBlank;

public class LoginUsuario {
    @NotBlank(message = "El email de usuario es obligatorio")
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
