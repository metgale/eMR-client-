package hr.foi.air.services;

/**
 * Comment model Comment attributes: id, comment, user_id, review_id
 * 
 * @author FRM
 * 
 */
public class CommentModel {
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getReview_id() {
		return review_id;
	}

	public void setReview_id(int review_id) {
		this.review_id = review_id;
	}

	public String comment;
	int user_id;
	int review_id;

}
