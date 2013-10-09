package hr.foi.air.services;

/**
 * Review model
 * Review attributes: username, id, movie, title, review, vote
 * 
 * @author FRM
 *
 */
public class ReviewModel {


	public String username;
	public int id;
	public String movie;
	public String title;
	public String review;
	public int vote;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMovie() {
		return movie;
	}

	public void setMovie(String movie) {
		this.movie = movie;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

}
