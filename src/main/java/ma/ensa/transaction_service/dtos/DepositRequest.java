package ma.ensa.transaction_service.dtos;

public class DepositRequest {
    private Long clientId;
    private String saveToken;
    private String numCard;
    private Double amount;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getSaveToken() {
        return saveToken;
    }

    public void setSaveToken(String saveToken) {
        this.saveToken = saveToken;
    }

    public String getNumCard() {
        return numCard;
    }

    public void setNumCard(String numCard) {
        this.numCard = numCard;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
