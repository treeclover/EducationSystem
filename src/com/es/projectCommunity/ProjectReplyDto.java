package com.es.projectCommunity;

public class ProjectReplyDto {

	private int reply_no;
	private int grno;
	private int seq;
	private int dep;
	private int project_no;
	private String content;
	private String writer;
	private String writer_name;
	private String write_time;
	private int parents_no;
	private int iqgrno;
	
	
	public String getWriter_name() {
		return writer_name;
	}
	public void setWriter_name(String writer_name) {
		this.writer_name = writer_name;
	}
	public int getParents_no() {
		return parents_no;
	}
	public void setParents_no(int parents_no) {
		this.parents_no = parents_no;
	}
	public int getIqgrno() {
		return iqgrno;
	}
	public void setIqgrno(int iqgrno) {
		this.iqgrno = iqgrno;
	}
	public int getReply_no() {
		return reply_no;
	}
	public void setReply_no(int reply_no) {
		this.reply_no = reply_no;
	}
	public int getGrno() {
		return grno;
	}
	public void setGrno(int grno) {
		this.grno = grno;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getDep() {
		return dep;
	}
	public void setDep(int dep) {
		this.dep = dep;
	}
	public int getProject_no() {
		return project_no;
	}
	public void setProject_no(int project_no) {
		this.project_no = project_no;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getWrite_time() {
		return write_time;
	}
	public void setWrite_time(String write_time) {
		this.write_time = write_time;
	}
	
	
}
