package classes.Person;

import java.io.serializable;
import java.util.Date;

/*
Card details

 */

public class Payment information implements serializable{
    private String cardNo;
    private String cvv;
    private String cardHolder;
    private Date expiryDate;

    //constructor with parameters
    public PaymentInformation(String cardNo,String cvv,String cardHolder){
        this.cardNo = cardNo;
        this.cvv = cvv;
        this.cardHolder = cardHolder;
    }
    //default constructor
    public PaymentInformation(){
        this("","","",null);
    }
    //getters
    public String getCardNo() {
        return cardNo;
    }

    public String getCVV() {
        return cvv;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    //setters
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setCVV(String cvv) {
        this.cvv = cvv;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
