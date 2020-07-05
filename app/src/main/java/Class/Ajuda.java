package Class;



public class Ajuda {

    private String keyPost;
    private String tipo;
    private String mensagem;
    private String autor;
    private String timestamp;
    private String emailAutor;
    private String statusAjuda;



    public Ajuda(){

    }

    public String getEmailAutor() {
        return emailAutor;
    }

    public void setEmailAutor(String emailAutor) {
        this.emailAutor = emailAutor;
    }

    public String getKeyPost() {
        return keyPost;
    }

    public void setKeyPost(String keyPost) {
        this.keyPost = keyPost;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatusAjuda() {
        return statusAjuda;
    }

    public void setStatusAjuda(String statusAjuda) {
        this.statusAjuda = statusAjuda;
    }
}
