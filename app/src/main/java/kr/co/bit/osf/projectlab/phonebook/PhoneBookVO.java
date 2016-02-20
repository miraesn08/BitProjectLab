package kr.co.bit.osf.projectlab.phonebook;

public class PhoneBookVO {
	private int id;
	private String name;
	private String phone;
	private String regDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	@Override
	public String toString() {
		return "PhoneBookVO [id=" + id + ", name=" + name + ", phone=" + phone + ", regDate=" + regDate + "]";
	}
}


/*
create sequence phonebook_seq;

create table phonebook(
id number primary key,
name varchar2(20) not null,
phone varchar2(20),
reg_date date
);

insert into phonebook
values (phonebook_seq.nextval, '김성박', '010-1234-5678', sysdate);

select * from phonebook;
*/