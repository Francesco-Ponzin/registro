package it.engim.fp.registro;

public class Vote {
	
	private int ID;
    private Course course;
    private User student;
    private int vote;
    private VoteStatus status;
	public int getId() {
		return ID;
	}
	public void setId(int id) {
		ID = id;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public User getStudent() {
		return student;
	}
	public void setStudent(User student) {
		this.student = student;
	}
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}
	public VoteStatus getStatus() {
		return status;
	}
	public void setStatus(VoteStatus status) {
		this.status = status;
	}
	

	
}
