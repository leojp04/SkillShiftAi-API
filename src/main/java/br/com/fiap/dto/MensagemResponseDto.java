package br.com.fiap.dto;

public class MensagemResponseDto {

    private String mensagem;

    public MensagemResponseDto() {}

    public MensagemResponseDto(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
