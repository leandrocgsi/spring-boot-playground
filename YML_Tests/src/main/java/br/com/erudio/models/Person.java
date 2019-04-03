package br.com.erudio.models;
 
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement
public class Person implements Serializable{
     
    private static final long serialVersionUID = 1L;
     
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
     
	public Person() {
	}
	
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
}