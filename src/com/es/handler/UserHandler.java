package com.es.handler;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.es.user.UserDao;
import com.es.user.UserDto;

@Controller
public class UserHandler {

	@Resource
	UserDao userDao;

	/*로그인 로직*/
	@RequestMapping(value="user/login", method = RequestMethod.POST)
	public String loginCheck(UserDto userDto, HttpSession session, Model model,
			HttpSession request, RedirectAttributes rttr) throws Exception{

		System.out.println("login emp_no : "+ userDto.getEmp_no());
		System.out.println("login password : "+ userDto.getPasswd());

		String emp_no = userDto.getEmp_no();
		String passwd = userDto.getPasswd();
		
		int result = userDao.loginCheck(emp_no, passwd);

		switch(result) {
		case -1 :
			rttr.addAttribute("msg", "fail");
			//model.addAttribute("result", "아이디나 비밀번호가 틀렸습니다.");
			System.out.println("case -1");
			break;
		case 0:
			//request.setAttribute("result", "아이디나 비밀번호가 틀렸습니다.");
			//model.addAttribute("result", "아이디나 비밀번호가 틀렸습니다.");
			rttr.addAttribute("msg", "fail");
			System.out.println("case 0");
			break;
		case 1:
			UserDto getUser = userDao.findById(emp_no, passwd);
			session.setAttribute("emp_no", getUser.getEmp_no());
			session.setAttribute("password", getUser.getPasswd());
			session.setAttribute("name",  getUser.getName());
			System.out.println("세션 등록.." + session);
			rttr.addFlashAttribute("result", "success");
			System.out.println("case 1");
			break;
		}

		return "redirect:/";
	}

	/*로그아웃 로직*/
	@RequestMapping(value="user/logout", method= RequestMethod.GET)
	public String logout(HttpSession session, Model model) {
		userDao.logout(session);
		return "redirect:/";
	}
	
	/* 비밀번호 변경 페이지 */
	@RequestMapping(value="user/password", method=RequestMethod.GET)
	public String passwordGet(Model model, @RequestParam("emp_no") String emp_no) {
		
		return "user/password";
	}
	
	/* 비밀번호 변경 로직*/
	@RequestMapping(value="user/password", method=RequestMethod.POST)
	public String passwordPost(Model model, @RequestParam("emp_no") String emp_no,
			@RequestParam("currentPasswd") String passwd,
			@ModelAttribute UserDto user) {
		System.out.println("기존 비밀번호 : " + passwd);
		System.out.println("변경할 비밀번호 : " + user.getCheckPasswd());

		UserDto userDto = new UserDto();
		userDto.setPasswd(passwd);
		userDto.setCheckPasswd(user.getCheckPasswd());
		int result = userDao.changePasswd(emp_no, userDto);
		if(result == 1) {
			System.out.println("pass 변경 성공");
		} else {
			System.out.println("pass 변경 실패");
		}
		
		return "redirect:/";
	}
	
	/* 기존 비밀번호 일치여부 확인 로직 */
	@RequestMapping(value="user/checkPassword", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String checkPassword(@ModelAttribute UserDto user, 
			HttpSession session, @RequestParam("passwd") String passwd) throws Exception {
		
		String emp_no = (String) session.getAttribute("emp_no");
		
		int result = userDao.checkPassword(emp_no, passwd);
		System.out.println("result:" + result);
		return String.valueOf(result);
	}
	
}
