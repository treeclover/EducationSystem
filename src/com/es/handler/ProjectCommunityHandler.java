package com.es.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.es.projectCommunity.ProjectCommunityDao;
import com.es.projectCommunity.ProjectCommunityDto;
import com.es.projectCommunity.ProjectReplyDto;

@Controller
public class ProjectCommunityHandler {

	
	@Resource
	private ProjectCommunityDao projectDao;
	
	@RequestMapping("/ProjectList")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Controller list");
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<ProjectCommunityDto> list;
		List<String> write_name = null;
		int totalList = 0;
		
		int spage = 1;
		if(request.getParameter("page") != null) 
			spage = Integer.parseInt(request.getParameter("page")); //현재페이지
		int start =spage*10-9; // 현재페이지 시작 페이징번호
		
		map.put("start",start-1);
		
		list = projectDao.projectList(map);
		
		/*검색 O*/
		if(request.getParameter("opt") !=null) { 
			String opt = request.getParameter("opt");
			String condition = request.getParameter("condition");
			request.setAttribute("condition", condition);
			map.put("opt",opt);
			map.put("condition",condition);
			list = projectDao.projectList(map);
			
		}
		
		
		totalList = projectDao.projectListCount(map);
		request.setAttribute("listCount", totalList); 
		
		
		System.out.println("totalList : "+totalList+" spage : "+spage);
		
		/*페이징 처리*/
		int maxPage = (int)(totalList/10.0+0.9); //전체페이지수
		int startPage = (int)(spage/5.0+0.8)*5-4; //시작페이지 번호
		int endPage= startPage+4;			//마지막 페이지 번호
		if(endPage > maxPage) endPage = maxPage;

		request.setAttribute("spage", spage);
		request.setAttribute("maxPage", maxPage);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		request.setAttribute("list", list);

		return new ModelAndView("project_community/list");
	}
	
	@RequestMapping("/ProjectDelete")
	public String ProjectDelte(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Controller ProjectDelete");
		
		
		/*세션*/
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("emp_no", "E2018040001");
		String emp_no =  (String) httpSession.getAttribute("emp_no");
		
		int project_no = Integer.parseInt(request.getParameter("project_no"));
		System.out.println(project_no);
		
		ProjectCommunityDto detail = projectDao.detailProject(project_no);
		
		if(detail.getFile_ori_name() != null) {
			File file = new File(detail.getFile_path());
			if(file.exists()==true) {
				file.delete();
			}
		}
		
		int result = projectDao.deleteProject(project_no);
		System.out.println("result : "+result);
		
		
		return "redirect:ProjectList.do";
	}

	@RequestMapping(value="/ProjectWrite", method=RequestMethod.GET)
	public ModelAndView ProjectWrite(HttpServletRequest req, HttpServletResponse rep) {
		System.out.println("Controller ProjectWrite GET");
		return new ModelAndView("project_community/write");
	}

	@RequestMapping(value="/ProjectWrite", method=RequestMethod.POST)
	public String ProjectWriteForm(HttpServletRequest req, HttpServletResponse rep) throws IllegalStateException, IOException {
		System.out.println("Controller ProjectWriteForm POST");
		/*세션*/
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("emp_no", "E2018040001");
		String emp_no =  (String) httpSession.getAttribute("emp_no");
		
		ProjectCommunityDto projectDto = new ProjectCommunityDto();
		
		String classification = req.getParameter("selectType");
		String title = req.getParameter("title");
		String content = req.getParameter("text");
		String writer = emp_no;
		String file_path= "";
		String file_save_name= "";
		String file_ori_name= "";
		
		projectDto.setClassification(classification);
		projectDto.setTitle(title);
		projectDto.setContent(content);
		projectDto.setWriter(writer);
		projectDto.setFile_path(file_path);
		projectDto.setFile_save_name(file_save_name);
		projectDto.setFile_ori_name(file_ori_name);
		
		// -- 파일 저장하기
		String path = req.getServletContext().getRealPath("/save");
		Map returnObject = new HashMap();
		
		System.out.println("path : "+path);
		
		MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest) req;
		Iterator<String> iterator = mhsr.getFileNames();

		System.out.println("mhsr.getFileNames() : " +mhsr.getFileNames());
		System.out.println("iterator : " +iterator);
		
		MultipartFile mfile = null;
		String fieldName = "";
		List resultList = new ArrayList();
		
		//디렉토리가 없으면 생성
		File dir = new File(path);
		if(!dir.isDirectory()) {
			dir.mkdirs();
		}
		
		while(iterator.hasNext()) {
			fieldName = iterator.next();
			
			mfile = mhsr.getFile(fieldName);
			
			String origName;
			
			/*origName = new String(mfile.getOriginalFilename().getBytes("8859_1"),"UTF-8");*/
			origName = new String(mfile.getOriginalFilename().getBytes("UTF-8"),"UTF-8");
			System.out.println(origName);
			
			//파일명이 없다면
			if("".equals(origName)) {
				continue;
			}
			
			System.out.println("origName : "+origName);
			
			
			//파일 명 변경(uuid로 암호화)
			String ext = origName.substring(origName.lastIndexOf('.'));//확장자
			String saveFileName = getUuid() + ext;
			
			System.out.println("saveFileName : "+saveFileName);
			
			
			//설정한 path에 파일저장
			File serverFile = new File(path+ File.separator + saveFileName);
			mfile.transferTo(serverFile);

			file_path= path+ File.separator + saveFileName;
			file_ori_name= origName;
			file_save_name= saveFileName;

			projectDto.setFile_path(file_path);
			projectDto.setFile_ori_name(file_ori_name);
			projectDto.setFile_save_name(file_save_name);
			
		}
		
		
		
		// write내용 넣기
		
		int result = projectDao.writeProject(projectDto);
		
		return "redirect:ProjectList.do";
		
	}


	public static String getUuid() {
		return UUID.randomUUID().toString().replaceAll("-","");
	}
	
	
	@RequestMapping("/ProjectDetail")
	public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		System.out.println("Controller ProjectDetail ");
		
		int project_no = Integer.parseInt(request.getParameter("project_no"));
		
		System.out.println(project_no);
		
		ProjectCommunityDto result = projectDao.detailProject(project_no);
		int hit = projectDao.updateHit(result);
		String user_name = projectDao.userName(result.getWriter());
		
		request.setAttribute("user_name", user_name);
		request.setAttribute("result", result);
		request.setAttribute("hit", hit);
		
		return new ModelAndView("project_community/detail");  
	}
	
	@RequestMapping("/ProjectFileDownload")
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int project_no = Integer.parseInt(request.getParameter("project_no"));
		ProjectCommunityDto result = projectDao.detailProject(project_no);
		//파일
		String path = result.getFile_path();
		String file_ori_name= new String(result.getFile_ori_name().getBytes("UTF-8"), "ISO-8859-1"); 
		
		/*String path = request.getParameter("path");*/
		File file = new File(path);

		/*response.setContentType(getContentType()); */
		response.setContentLength((int)file.length());

		System.out.println("file.getName() : "+file.getName());
		
		
		response.setHeader("Content-Disposition", "attachment; filename=\""+file_ori_name+"\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		
		
		fis = new FileInputStream(file);
		FileCopyUtils.copy(fis, out);
		
		if(fis != null) {
			fis.close();
		}
		
		out.flush();
		
	}
	
	@RequestMapping(value="/ProjectModify", method=RequestMethod.GET)
	public ModelAndView ProjectModify(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Controller ProjectModify GET");
		int project_no = Integer.parseInt(request.getParameter("project_no"));
		
		ProjectCommunityDto result = projectDao.detailProject(project_no);
		System.out.println(result);
		request.setAttribute("result", result);
		
		return new ModelAndView("project_community/modify");
	}

	@RequestMapping(value="/ProjectModify", method=RequestMethod.POST)
	public String ProjectModifyForm(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		System.out.println("Controller ProjectModify POST");
		int project_no = Integer.parseInt(request.getParameter("project_no"));
		ProjectCommunityDto detail = projectDao.detailProject(project_no);
		
		
		/*세션등록*/
		/*세션*/
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("emp_no", "E2018040001");
		String emp_no =  (String) httpSession.getAttribute("emp_no");
		
		ProjectCommunityDto projectDto = new ProjectCommunityDto();
		
		String classification = request.getParameter("selectType");
		String title = request.getParameter("title");
		String content = request.getParameter("text");
		String writer = emp_no;
		String file_path= "";
		String file_save_name= "";
		String file_ori_name= "";
		
		projectDto.setProject_no(project_no);
		projectDto.setClassification(classification);
		projectDto.setTitle(title);
		projectDto.setContent(content);
		projectDto.setWriter(writer);
		
		
		System.out.println("classification : "+classification+" title : "+title+" content : "+content+" writer : "+writer);
		
		
		// -- 파일 저장하기
		
		
		
		String path = request.getServletContext().getRealPath("/save");
		
		MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = mhsr.getFileNames();

		System.out.println("mhsr.getFileNames() : " +mhsr.getFileNames());
		System.out.println("iterator : " +iterator);
		
		
		MultipartFile mfile = null;
		String fieldName = "";
		
		//디렉토리가 없으면 생성
		File dir = new File(path);
		if(!dir.isDirectory()) {
			dir.mkdirs();
		}
		
		while(iterator.hasNext()) {
			fieldName = iterator.next();
			
			mfile = mhsr.getFile(fieldName);
			
			String origName;
			
			origName = new String(mfile.getOriginalFilename().getBytes("UTF-8"),"UTF-8");
			//파일명이 없다면
			if("".equals(origName)) {
				continue;
			}
			
			System.out.println("origName : "+origName);
			
			
			//파일 명 변경(uuid로 암호화)
			String ext = origName.substring(origName.lastIndexOf('.'));//확장자
			String saveFileName = getUuid() + ext;
			
			System.out.println("saveFileName : "+saveFileName);
			
			
			//설정한 path에 파일저장
			File serverFile = new File(path+ File.separator + saveFileName);
			mfile.transferTo(serverFile);

			file_path= path+ File.separator + saveFileName;
			file_save_name= saveFileName;
			file_ori_name= origName;

			projectDto.setFile_path(file_path);
			projectDto.setFile_save_name(file_save_name);
			projectDto.setFile_ori_name(file_ori_name);
			
		}
		
		if(file_ori_name.equals("")) {
			projectDto.setFile_path(detail.getFile_path());
			projectDto.setFile_ori_name(detail.getFile_ori_name());
			projectDto.setFile_save_name(detail.getFile_save_name());
		}
		
		System.out.println("projectDto.getClassification() : "+projectDto.getClassification());
		System.out.println("projectDto.getContent() : "+projectDto.getContent());
		System.out.println("projectDto.getFile_path() : "+projectDto.getFile_path());
		System.out.println("projectDto.getProject_no() :"+projectDto.getProject_no());
		System.out.println("projectDto.getTitle() : "+projectDto.getTitle());
		System.out.println("projectDto.getWriter() :"+projectDto.getWriter());
		
		
		
		int result = projectDao.modifyProject(projectDto);
		
		System.out.println("result : " +result);
		
		return "redirect:ProjectDetail.do?project_no="+project_no;
	}
	
	
	
	
	/*comment*/
	
	/*댓글 쓰기*/
	@RequestMapping(value="EducationList/CommentWrite", method=RequestMethod.GET)
	public void CommentWrite(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		System.out.println("Controller CommentWrite GET");
		
		/*세션*/
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("emp_no", "E2018040001");
		httpSession.setAttribute("name", "juhyun");
		
		
		String writer =  (String) httpSession.getAttribute("emp_no");
		String writer_name =  (String) httpSession.getAttribute("name");
		String content = request.getParameter("content");
		int project_no = Integer.parseInt(request.getParameter("project_no"));
		int reply_no = -1;
		
		
		ProjectReplyDto replyDto = new ProjectReplyDto();
		
		replyDto.setProject_no(project_no);
		replyDto.setSeq(1);
		replyDto.setGrno(0);
		replyDto.setDep(0);
		
		replyDto.setContent(content);
		replyDto.setWriter(writer);
		replyDto.setWriter_name(writer_name);

		//답글의 댓글이라면 
		if(request.getParameter("reply_no") != null) {
			reply_no = Integer.parseInt(request.getParameter("reply_no"));
			List<ProjectReplyDto> list = projectDao.commentList(project_no);
			int i=0; 	// list의 인덱스 
			
			for(i=0; list.size() > i; i++) {
				System.out.println("list.get(i).getReply_no() : "+list.get(i).getReply_no());
			
				if(reply_no == list.get(i).getReply_no())
					break;
			}
			
			replyDto.setGrno(list.get(i).getGrno()); //부모의 그룹번호
			replyDto.setSeq(list.get(i).getSeq());
			replyDto.setDep(list.get(i).getDep()+1);
			replyDto.setParents_no(list.get(i).getReply_no());
			
			int updateReq = projectDao.updateReq(replyDto);
			System.out.println(" 답글 updateReq "+updateReq);
			
		}

		System.out.println("content : "+content+" writer : "+writer+" project_no : "+project_no+" reply_no : "+reply_no);
		
		// write내용 넣기
		int result = projectDao.CommentWrite(replyDto);
		System.out.println("result : "+result);
	}
	
	
	/* 댓글 목록 띄우기 */
	@RequestMapping("/EducationList/CommentList")
	@ResponseBody
	public List<ProjectReplyDto> Commentlist(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Controller CommentList");
		/*세션*/
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("emp_no", "E2018040001");
		httpSession.setAttribute("name", "juhyun");
		
		
		int project_no = Integer.parseInt(request.getParameter("project_no"));
		List<ProjectReplyDto> list = projectDao.commentList(project_no);

		request.setAttribute("list", list);
		
		return list;
	}

	/* 댓글 수정 */
	@RequestMapping("/EducationList/CommentUpdate")
	public void CommentUpdate(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		System.out.println("Controller CommentUpdate");
		
		int reply_no = Integer.parseInt(request.getParameter("reply_no"));
		String content = request.getParameter("content");
		
		map.put("content", content);
		map.put("reply_no", reply_no);
		
		projectDao.CommentUpdate(map);
		
		System.out.println("reply_no : "+reply_no+" content : "+content);
		
	}

	@RequestMapping("/EducationList/CommentDelete")
	public void CommentDelete(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Controller CommentDelete");
		int reply_no = Integer.parseInt(request.getParameter("reply_no"));
		HashMap<String, Object> map = new HashMap<String, Object>(); 
		int project_no = Integer.parseInt(request.getParameter("project_no"));
		
		int tmp = reply_no;
		
		
		/*답글 삭제 클릭시 자식  모두 삭제*/
		while(true) {
			List<ProjectReplyDto> list = projectDao.commentList(project_no);
			
			int sum =0;
			int parents_no =0;
			
			for(int k=0; list.size() > k; k++) {
				
				parents_no =list.get(k).getParents_no();

				if(tmp == list.get(k).getParents_no()) {
					tmp = list.get(k).getReply_no();
					break;
				}
				
			}
			
			
			for(int j=0; list.size() >j; j++) {
				if(tmp == list.get(j).getParents_no()) sum += 1;
			}

			if(sum == 0) {
				
				map.put("delete_no", tmp);
				
				projectDao.CommentDelete(map);
				if(reply_no == tmp ) break;

				tmp = parents_no;
				
			}
					
		}
		
		
	}

	
}
