package api;

/**
 * Defines the fields present in the JSON body of responses from the /collect_purchase_info endpoint.
 */
public class CollectPurchaseInfoResponse {
    private final String customerFirstName;
    private final String customerLastName;
    private final String customerEmail;
    private final String purchaseDate;
    private final String vrHeadsetName;
    private final String vrHeadsetPrice;
    private final int vrHeadsetTrialDays;

    public CollectPurchaseInfoResponse(String customerFirstName, String customerLastName, String customerEmail,
                                       String purchaseDate, String vrHeadsetName, String vrHeadsetPrice,
                                       int vrHeadsetTrialDays) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
        this.purchaseDate = purchaseDate;
        this.vrHeadsetName = vrHeadsetName;
        this.vrHeadsetPrice = vrHeadsetPrice;
        this.vrHeadsetTrialDays = vrHeadsetTrialDays;
    }

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

    public String getVrHeadsetPrice() {
        return vrHeadsetPrice;
    }

    public int getVrHeadsetTrialDays() {
        return vrHeadsetTrialDays;
    }
}
