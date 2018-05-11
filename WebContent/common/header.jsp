<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Education System</title>

<link rel="StyleSheet" href="/EducationSystem/css/bootstrap.min.css">
<script>
	
	function check(){
		alert("click");
		
	}
</script>
</head>
<body style="width: 70%; margin: 0 auto;">
	<div style="text-align: right">
		<c:choose>
		  <c:when test="${ sessionScope.emp_no == null }">
				<button type="button" class="dynamic-button" data-toggle="modal" data-target="#login" tabindex="0"
					style="width: 117px; height: 40px;">로그인</button>
		  </c:when>
		  <c:otherwise>
		  	안녕하세요 ${sessionScope.name}님
			<button type="button" class="dynamic-button" 
					onclick="location.href='${path}/user/logout.do'"
					 tabindex="0" style="width: 117px; height: 40px;">로그아웃</button>
		  </c:otherwise>
		</c:choose>
	</div>
	<div class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-responsive-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${path}/main.do">HOME</a>
		</div>
		<div class="navbar-collapse collapse navbar-responsive-collapse">
			<ul class="nav navbar-nav">
				<li><a href="${path}/#">공지사항</a></li>
				<li><a href="${path}/EducationList.do">교육목록/신청</a></li>
				<li><a href="${path}/PetitionList.do">청원</a></li>
				<li><a href="${path}/eduhistory.do">수강목록</a></li>
				<li><a href="${path}/instructor/main.do">강사</a></li>
				<li><a href="${path}/ProjectList.do">프로젝트공유</a></li>
			</ul>
		</div>
	</div>
	<!-- 모달 팝업 -->
	<div class="modal fade" id="login" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" 
		 aria-hidden="true">
		<div class="modal-dialog"  style="width: 400px; margin-top: 180px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">로그인</h4>
				</div>

				<div class="modal-body">
					<!-- 로그인부트스트랩  -->
					<div class="card card-container" align="center">
						<img src="http://www.bitacademy.com/Images/Content/Employ/img_LOGO_BizTechPartners.png" height="40px"/>
						
						<img id="profile-img" class="profile-img-card"
							src="https://ssl.gstatic.com/accounts/ui/avatar_2x.png" />
						<p id="profile-name" class="profile-name-card"></p>

						<!-- form -->
						<form id="loginForm" name="loginForm" class="form-signin"
							method="post" action="${path}/user/login.do" onsubmit="check();">
							<span id="reauth-email" class="reauth-email"></span>
							
							<input type="text" name="emp_no" id="emp_no" class="form-control" placeholder="ID" required autofocus value=""> 
							
							<input type="password" name="password" id="password" class="form-control" placeholder="Password" required>
							<br>
							<button class="btn btn-lg btn-primary btn-block btn-signin"
								type="submit">Sign in</button>
						</form>
						<!-- /form -->
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</body>