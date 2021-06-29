package lunnardo.jwork_android;

public class Bonus {
    private int id;
    private String referralCode;
    private int extraFee;
    private int minTotalFee;
    private boolean active;

    public Bonus(int id, String referralCode, int extraFee, int minTotalFee, boolean active) {
        this.id = id;
        this.referralCode = referralCode;
        this.extraFee = extraFee;
        this.minTotalFee = minTotalFee;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public int getExtraFee() {
        return extraFee;
    }

    public int getMinTotalFee() {
        return minTotalFee;
    }

    public boolean getActive() {
        return active;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public void setExtraFee(int extraFee) {
        this.extraFee = extraFee;
    }

    public void setMinTotalFee(int minTotalFee) {
        this.minTotalFee = minTotalFee;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
