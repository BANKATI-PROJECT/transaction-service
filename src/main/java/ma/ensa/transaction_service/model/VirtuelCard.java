package ma.ensa.transaction_service.model;

import java.time.LocalDate;

public class VirtuelCard {
    private Long id;
    private String num;
    private String cvv;
    private LocalDate expire;
    private Double solde;
    private Long clientId;

    public Long getId() {
        return id;
    }

    public String getNum() {
        return num;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDate getExpire() {
        return expire;
    }

    public Double getSolde() {
        return solde;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setExpire(LocalDate expire) {
        this.expire = expire;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
