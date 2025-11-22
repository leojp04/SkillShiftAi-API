package br.com.fiap.exception;

public class ErrorResponseDto {

    private String mensagem;

    public ErrorResponseDto() {}

    public ErrorResponseDto(String mensagem) {
        this.mensagem = mensagem;
    }

    // getters e setters


    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
