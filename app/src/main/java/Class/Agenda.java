package Class;

public class Agenda {

    private String keyAtividade;
    private String dataEntrega;
    private String nomeAtividade;
    private String statusAtividade;
    private String textoAtividade;
    private String autorAtividade;

    public Agenda(String nomeAtividade , String textoAtividade, String dataEntrega){

        this.nomeAtividade = nomeAtividade;
        this.textoAtividade = textoAtividade;
        this.dataEntrega = dataEntrega;


    }

    public String getKeyAtividade() {
        return keyAtividade;
    }

    public void setKeyAtividade(String keyAtividade) {
        this.keyAtividade = keyAtividade;
    }

    public String getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getNomeAtividade() {
        return nomeAtividade;
    }

    public void setNomeAtividade(String nomeAtividade) {
        this.nomeAtividade = nomeAtividade;
    }

    public String getStatusAtividade() {
        return statusAtividade;
    }

    public void setStatusAtividade(String statusAtividade) {
        this.statusAtividade = statusAtividade;
    }

    public String getTextoAtividade() {
        return textoAtividade;
    }

    public void setTextoAtividade(String textoAtividade) {
        this.textoAtividade = textoAtividade;
    }

    public String getAutorAtividade() {
        return autorAtividade;
    }

    public void setAutorAtividade(String autorAtividade) {
        this.autorAtividade = autorAtividade;
    }
}
