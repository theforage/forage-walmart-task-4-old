package api;

/**
 * Defines the fields present in the JSON body of responses from the /record_purchase endpoint.
 */
public class RecordPurchaseResponse {
    private final boolean success;

    public RecordPurchaseResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
