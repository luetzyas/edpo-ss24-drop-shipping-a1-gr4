package io.drop.shipping.mailing.messages.payload;

public class OrderItemPayload {

    /**
     * {
     *      "articleId":"article1",
     *      "amount":5
     * }
     */

    private String articleId;

    private int amount;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
