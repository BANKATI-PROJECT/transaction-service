package ma.ensa.transaction_service.model;

import java.util.List;

public class Portefeuille {
    private String id;
    private Double solde;
    private String plafond;
    private String currency;
    private Long clientId;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    private List<VirtuelCard> virtuelCards;

    public String getId() {
        return id;
    }

    public Double getSolde() {
        return solde;
    }

    public String getPlafond() {
        return plafond;
    }

    public String getCurrency() {
        return currency;
    }

    public List<VirtuelCard> getVirtuelCards() {
        return virtuelCards;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public void setPlafond(String plafond) {
        this.plafond = plafond;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setVirtuelCards(List<VirtuelCard> virtuelCards) {
        this.virtuelCards = virtuelCards;
    }
}
