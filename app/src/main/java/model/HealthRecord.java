package model;

public class HealthRecord
{
    String recordNumber;
    String recordDate;
    String subAx;
    String objAx;
    String analysis;
    String problemList;
    String intervention;
    String evaluation;
    String review;
    String staffID;


    public HealthRecord(){}

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getSubAx() {
        return subAx;
    }

    public void setSubAx(String subAx) {
        this.subAx = subAx;
    }

    public String getObjAx() {
        return objAx;
    }

    public void setObjAx(String objAx) {
        this.objAx = objAx;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getProblemList() {
        return problemList;
    }

    public void setProblemList(String problemList) {
        this.problemList = problemList;
    }

    public String getIntervention() {
        return intervention;
    }

    public void setIntervention(String intervention) {
        this.intervention = intervention;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }
}
