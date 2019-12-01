package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "DOCUMENTSTORE")
public class DocumentStore implements Serializable {

	private static final long serialVersionUID = 7700110758724840308L;
	
	@Id
	@Column(name = "DOCUMENTID")
	@SequenceGenerator(name = "generator", sequenceName = "document_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
	private Long documentId;
	
	@Column(name = "DOCUMENTSTORECODE")
	private String documentStoreCode;
	
	@Column(name = "DOCUMENTPATH")
	private String documentPath;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDDATE")
	private Date createdDate = new Date();
}
