package kr.co.bit.osf.projectlab.phonebook;

public class PhoneBookResult {
	private int errorCode;
	private String errorMessage;
	private PhoneBookVO phoneBook;
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public PhoneBookVO getPhoneBook() {
		return phoneBook;
	}
	public void setPhoneBook(PhoneBookVO phoneBook) {
		this.phoneBook = phoneBook;
	}
	
	@Override
	public String toString() {
		return "PhoneBookResult [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", phoneBook=" + phoneBook
				+ "]";
	}
}
