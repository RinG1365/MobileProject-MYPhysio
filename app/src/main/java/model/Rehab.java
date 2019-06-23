package model;

public class Rehab {
    String rehabID;
    String rehabPlanID;
    String rehabDescription;
    String rehabVideo;
    String rehabstaffid;
    String rehabPhyName;
    String evLink;
    String evDate;
    String evFeedback;

    public Rehab(){

    }
    public String getRehabID() {
        return rehabID;
    }

    public void setRehabID(String rehabID) {
        this.rehabID = rehabID;
    }

    public String getRehabPlanID() {
        return rehabPlanID;
    }

    public void setRehabPlanID(String rehabPlanID) {
        this.rehabPlanID = rehabPlanID;
    }

    public String getRehabDescription() {
        return rehabDescription;
    }

    public void setRehabDescription(String rehabDescription) {
        this.rehabDescription = rehabDescription;
    }

    public String getRehabVideo() {
        return rehabVideo;
    }

    public void setRehabVideo(String rehabVideo) {
        this.rehabVideo = rehabVideo;
    }

    public String getRehabstaffid() {
        return rehabstaffid;
    }

    public void setRehabstaffid(String rehabstaffid) {
        this.rehabstaffid = rehabstaffid;
    }

    public String getEvLink() {
        return evLink;
    }

    public void setEvLink(String evLink) {
        this.evLink = evLink;
    }

    public String getEvDate() {
        return evDate;
    }

    public void setEvDate(String evDate) {
        this.evDate = evDate;
    }

    public String getEvFeedback() {
        return evFeedback;
    }

    public String getRehabPhyName() {
        return rehabPhyName;
    }

    public void setRehabPhyName(String rehabPhyName) {
        this.rehabPhyName = rehabPhyName;
    }

    public void setEvFeedback(String evFeedback) {
        this.evFeedback = evFeedback;
    }
}
