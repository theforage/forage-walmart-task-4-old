package api;

/**
 * Defines expected fields in the JSON body of requests to the /record_purchase endpoint.
 */
public class RecordPurchaseRequest {
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String purchaseDate;
    private String vrHeadsetName;

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getVrHeadsetName() {
        return vrHeadsetName;
    }
}
