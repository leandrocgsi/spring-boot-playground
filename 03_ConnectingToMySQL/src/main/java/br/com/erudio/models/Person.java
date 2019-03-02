package br.com.erudio.models;
 
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
 
@Entity
@Table (name="person")
public class Person implements Serializable{
     
    private static final long serialVersionUID = 1L;
     
    @Id
    @GeneratedValue
    @Column(name="id", nullable=false)
    private Long id;
    
    @Column (name="first_name", nullable = false, length = 80 )
    private String firstName;
    
    @Column (name="last_name", nullable = false, length = 80 )
    private String lastName;
    
    @Column (name="address", nullable = false, length = 80 )
    private String address;
    
    
    @Column (name="gender", nullable = false, length = 6 )
    private String gender;
     
    public Long getId() {
        return id;
    }
     
    public void setId(Long id) {
        this.id = id;
    }
     
    public String getFirstName() {
        return firstName;
    }
     
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
     
    public String getLastName() {
        return lastName;
    }
     
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
     
    public String getAddress() {
        return address;
    }
     
    public void setAddress(String address) {
        this.address = address;
    }

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
    
    
}