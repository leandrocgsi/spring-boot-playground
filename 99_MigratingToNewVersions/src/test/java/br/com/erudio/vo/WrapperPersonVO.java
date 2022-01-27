package br.com.erudio.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperPersonVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("_embedded")
	private PersonEmbeddedVO embedded;

	public WrapperPersonVO() {}

	public PersonEmbeddedVO getEmbedded() {
		return embedded;
	}

	public void setEmbedded(PersonEmbeddedVO embedded) {
		this.embedded = embedded;
	}
}
