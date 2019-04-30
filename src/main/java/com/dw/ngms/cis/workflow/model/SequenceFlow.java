package com.dw.ngms.cis.workflow.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@XmlRootElement
public class SequenceFlow {
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("state")
	private String state;
	@JsonProperty("targetState")
	private String targetState;
	@JsonProperty("restDependancy")
	private String restDependancy;
	@JsonProperty("assigneeList")
	private List<Assignee> assigneeList;
	@JsonProperty("targetList")
	private List<Target> targetList = new ArrayList<Target>();
		
}
