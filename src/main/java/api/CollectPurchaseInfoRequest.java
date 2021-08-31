package api;

/**
 * Defines expected fields in the JSON body of requests to the /collect_purchase_info endpoint.
 */
public class CollectPurchaseInfoRequest {
    private int purchaseId;

    public int getPurchaseId() {
        return purchaseId;
    }
}
