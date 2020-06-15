package Class;

public class Agenda {


    private String agendaKey;
    private String nomeAtividade;
    private String textoAtividade;
    private String dataEntrega;
    private String statusAtividade;
    private String autorAtividade;

    public Agenda(String nomeAtividade , String textoAtividade, String dataEntrega, String autorAtividade, String statusAtividade){

        this.nomeAtividade = nomeAtividade;
        this.textoAtividade = textoAtividade;
        this.dataEntrega = dataEntrega;
        this.autorAtividade = autorAtividade;
        this.statusAtividade = statusAtividade;

    }

    public Agenda(){

    }

    public String getNomeAtividade() {
        return nomeAtividade;
    }

    public String getTextoAtividade() {
        return textoAtividade;
    }

    public String getDataEntrega() {
        return dataEntrega;
    }

    public String getStatusAtividade() {
        return statusAtividade;
    }

    public String getAutorAtividade() {
        return autorAtividade;
    }

    public void setNomeAtividade(String nomeAtividade) {
        this.nomeAtividade = nomeAtividade;
    }

    public void setTextoAtividade(String textoAtividade) {
        this.textoAtividade = textoAtividade;
    }

    public void setDataEntrega(String dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public void setStatusAtividade(String statusAtividade) {
        this.statusAtividade = statusAtividade;
    }

    public void setAutorAtividade(String autorAtividade) {
        this.autorAtividade = autorAtividade;
    }

    public String getAgendaKey() {
        return agendaKey;
    }

    public void setAgendaKey(String agendaKey) {
        this.agendaKey = agendaKey;
    }
}
