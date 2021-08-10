package com.escanor.acadperfapi.models;

public class Comment {
    private Integer commentId;
    private Integer reportId;
    private String description;

    public Comment(Integer commentId, Integer reportId, String description) {
        this.commentId = commentId;
        this.reportId = reportId;
        this.description = description;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
