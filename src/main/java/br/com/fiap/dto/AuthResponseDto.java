package br.com.fiap.dto;

public class AuthResponseDto {

    private String token;
    private SimpleUserDto usuario;

    public AuthResponseDto() {}

    public AuthResponseDto(String token, SimpleUserDto usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    // getters e setters


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SimpleUserDto getUsuario() {
        return usuario;
    }

    public void setUsuario(SimpleUserDto usuario) {
        this.usuario = usuario;
    }
}
