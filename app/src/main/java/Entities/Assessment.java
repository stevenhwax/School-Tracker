package Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity (tableName = "assessment_table")
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    private Integer assessmentId;

    private String assessmentName;
    private LocalDateTime assessmentStart;
    private LocalDateTime assessmentEnd;
    private String assessmentType; //Objective, Performance

    public Assessment(String assessmentName, LocalDateTime assessmentStart, LocalDateTime assessmentEnd, String assessmentType) {
        this.assessmentName = assessmentName;
        this.assessmentStart = assessmentStart;
        this.assessmentEnd = assessmentEnd;
        this.assessmentType = assessmentType;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public LocalDateTime getAssessmentStart() {
        return assessmentStart;
    }

    public void setAssessmentStart(LocalDateTime assessmentStart) {
        this.assessmentStart = assessmentStart;
    }

    public LocalDateTime getAssessmentEnd() {
        return assessmentEnd;
    }

    public void setAssessmentEnd(LocalDateTime assessmentEnd) {
        this.assessmentEnd = assessmentEnd;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String toString() {
        return assessmentId + " " + assessmentName;
    }
}
