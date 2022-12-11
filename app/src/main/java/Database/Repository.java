package Database;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DAO.AssessmentDAO;
import DAO.CourseDAO;
import DAO.TermDAO;
import Entities.Assessment;
import Entities.Course;
import Entities.Term;

public class Repository {

    private TermDAO mTermDAO;
    private CourseDAO mCourseDAO;
    private AssessmentDAO mAssessmentDAO;
    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    private List<Assessment> mAllAssessments;

    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application app) {
        DatabaseBuilder db = DatabaseBuilder.getDatabase(app);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mAssessmentDAO = db.assessmentDAO();
    }

    public void insert(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.insert(term);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insert(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.insert(course);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insert(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.update(term);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.update(course);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.update(assessment);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.delete(term);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.delete(course);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.delete(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Term> getAllTerms(){
        databaseExecutor.execute(()->{
            mAllTerms = mTermDAO.getAllTerms();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllTerms;
    }

    public List<Course> getAllCourses(){
        databaseExecutor.execute(()->{
            mAllCourses = mCourseDAO.getAllCourses();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllCourses;
    }

    public List<Assessment> getAllAssessments(){
        databaseExecutor.execute(()->{
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAssessments;
    }

    public Term getTermById(Integer id){
        if(mAllTerms==null){
            getAllTerms();
        }
        if(mAllTerms != null) {
            for(Term term : mAllTerms){
                if(id == term.getTermId()){
                    return term;
                }
            }
        }
        return null;
    }

    public Course getCourseById(Integer id){
        if(mAllCourses == null){
            getAllCourses();
        }
        if(mAllCourses != null){
            for(Course course : mAllCourses){
                if(id == course.getCourseId()){
                    return course;
                }
            }
        }
        return null;
    }

    public Assessment getAssessmentById(Integer id){
        if(mAllAssessments == null){
            getAllAssessments();
        }
        if(mAllAssessments != null){
            for(Assessment assessment : mAllAssessments){
                if(id == assessment.getAssessmentId()){
                    return assessment;
                }
            }
        }
        return null;
    }

    public List<Integer> getAssociatedTerms(Course course){
        List<Integer> result = new ArrayList<>();
        if(mAllTerms==null){
            getAllTerms();
        }
        for(Term term : mAllTerms){
            for(Integer c : term.getAssociatedCourses()){
                if(course.getCourseId() == c){
                    result.add(term.getTermId());
                }
            }
        }
        return result;
    }

    public List<Integer> getAssociatedCourses(Assessment assessment){
        List<Integer> result = new ArrayList<>();
        if (mAllCourses == null){
            getAllCourses();
        }
        for(Course course : mAllCourses) {
            for(Integer a : course.getAssociatedAssessments()){
                if(assessment.getAssessmentId() == a){
                    result.add(course.getCourseId());
                }
            }
        }
        return result;
    }
}
