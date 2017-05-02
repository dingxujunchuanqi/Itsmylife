package com.sinoautodiagnoseos.entity.Feedback;

import java.util.List;

/**
 * Created by Lanye on 2017/3/9.
 */

public class Feedback {
    public String memberId;
    public String content;
    public List<Feedbackfiles> feedBackFilesDto;

    public String getMemberId() {
        return memberId;
    }

    public String getContent() {
        return content;
    }

    public void setFeedBackFilesDto(List<Feedbackfiles> feedBackFilesDto) {
        this.feedBackFilesDto = feedBackFilesDto;
    }

    public List<Feedbackfiles> getFeedBackFilesDto() {

        return feedBackFilesDto;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
