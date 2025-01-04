package ma.ensa.transaction_service.model;

import java.util.List;

public class Portefeuille {
    private Long id;
    private Double solde;
    private Double plafond;
    private String currency;
    private Long clientId;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    private List<VirtuelCard> virtuelCards;

    public Long getId() {
        return id;
    }

    public Double getSolde() {
        return solde;
    }

    public Double getPlafond() {
        return plafond;
    }

    public String getCurrency() {
        return currency;
    }

    public List<VirtuelCard> getVirtuelCards() {
        return virtuelCards;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public void setPlafond(Double plafond) {
        this.plafond = plafond;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setVirtuelCards(List<VirtuelCard> virtuelCards) {
        this.virtuelCards = virtuelCards;
    }
}
