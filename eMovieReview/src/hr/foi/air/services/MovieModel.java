package hr.foi.air.services;

/**
 * Movie Model Move attributes: id, title, poster, review
 * 
 * @author FRM
 * 
 */
public class MovieModel {

	public int id;
	public String title;
	public String poster;
	public String review;

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}
}
