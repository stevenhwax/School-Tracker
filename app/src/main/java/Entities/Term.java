package Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.List;

@Entity (tableName = "term_table")
public class Term {

    @PrimaryKey(autoGenerate = true)
    private Integer termId;

    private String termName;
    private LocalDate termStart;
    private LocalDate termEnd;
    private List<Integer> associatedCourses;

    public Term(String termName, LocalDate termStart, LocalDate termEnd, List<Integer> associatedCourses) {
        this.termName = termName;
        this.termStart = termStart;
        this.termEnd = termEnd;
        this.associatedCourses = associatedCourses;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public LocalDate getTermStart() {
        return termStart;
    }

    public void setTermStart(LocalDate termStart) {
        this.termStart = termStart;
    }

    public LocalDate getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(LocalDate termEnd) {
        this.termEnd = termEnd;
    }

    public List<Integer> getAssociatedCourses() {
        return associatedCourses;
    }

    public void setTermCourses(List<Integer> termCourses) {
        this.associatedCourses = associatedCourses;
    }

    public String toString(){
        return termId + ": " + termName;
    }
}
