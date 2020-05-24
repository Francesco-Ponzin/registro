package it.engim.fp.registro;

public class DAOException extends Exception {

	private static final long serialVersionUID = 1L;
	private String error;
	public DAOException(String error) {
		super();
		this.error = error;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	@Override
	public String toString() {
		return error;
	}
	
	
}
