package kr.co.bit.osf.projectlab.phonebook;

import java.util.List;

public class PhoneBookListResult {
	private int errorCode;
	private String errorMessage;
	private List<PhoneBookVO> phoneBookList;
	
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
	public List<PhoneBookVO> getPhoneBookList() {
		return phoneBookList;
	}
	public void setPhoneBookList(List<PhoneBookVO> phoneBookList) {
		this.phoneBookList = phoneBookList;
	}
	
	@Override
	public String toString() {
		return "PhoneBookListResult [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", phoneBookList="
				+ phoneBookList + "]";
	}
}
