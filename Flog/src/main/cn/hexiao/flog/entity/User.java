package cn.hexiao.flog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Email;


@Entity
@Table(name="flog_user")
public class User extends PersistentObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1978635623974424277L;
	private Long id ;
	private String name;
	private String password;
	private String email;
	private String msn;
	private String qq;
	
	private boolean logedin = false;
	
	@Transient
	public boolean isLogedin() {
		return logedin;
	}
	public void setLogedin(boolean logedin) {
		this.logedin = logedin;
	}
	public User() {
		
	}
	public User(String name, String password, String email, String msn,
			String qq) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
		this.msn = msn;
		this.qq = qq;
	}
	
	@Override
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Email
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMsn() {
		return msn;
	}
	public void setMsn(String msn) {
		this.msn = msn;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	@Override
	public void setData(PersistentObject obj) {
		User u = (User) obj;
		this.email = u.getEmail();
		this.id = u.getId();
		this.msn = u.getMsn();
		this.name = u.getName();
		this.password = u.getPassword();
		this.qq = u.getQq();
		
	}
	
	

}
