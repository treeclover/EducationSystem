<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"
	scope="application" />
<title>메인 페이지 - Education System</title>
<jsp:include page="common/header.jsp" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<!-- 로그인모달 -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<!-- 드롭다운(비밀번호 변경) -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style>
.boxA:after {
	content: "";
	display: block;
	clear: both
}

.box1 {
	float: left;
	width: 500px;
	height: 300px;
}

.box2 {
	float: left;
	width: 500px;
	height: 300px;
}

.box3 {
	float: left;
	width: 500px;
	height: 300px;
}

.box4 {
	float: left;
	width: 500px;
	height: 300px;
}
th{text-align: center;}
</style>
<script>
	
</script>
<body>


	<div class="boxA">
		<div class="box1">
			<h5>
				<a href="${path}/notice.do">공지사항</a>
			</h5>
			<hr>
			<table>
				<tr>
					<th align="center" style="width: 10%;">번호</th>
					<th align="center" style="width: 10%;">제목</th>
					<th align="center" style="width: 15%;">작성자</th>
					<th align="center" style="width: 15%;">등록일</th>
					<th align="center" style="width: 10%;">조회수</th>

				</tr>


				<c:choose>
					<c:when test="${empty requestScope.noticelist }">
						<tr>
							<td align="center" colspan="9">공지사항에 저장된 글이 없습니다. 글쓰기를 눌러
								주세요</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${requestScope.noticelist}" var="result">
							<tr>
								<td align="center">${result.notice_no }</td>
								<td align="center"><a
									href="detail.do?notice_no=${result.notice_no}">
										${result.title }</a></td>
								<td align="center">${result.writer }</td>
								<td align="center">${result.write_time }</td>
								<td align="center">${result.hit }</td>

							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</table>

		</div>

		<div class="box2">
			<h5>
				<a href="${path}/EducationList.do">교육리스트</a>
			</h5>
			<hr>
			<table>
				<tr>
					<th align="center" style="width: 10%;">번호</th>
					<th align="center" style="width: 40%;">교육명</th>
					<th align="center" style="width: 40%;">교육일</th>
					<th align="center" style="width: 10%;">장소</th>
				</tr>
				<c:choose>
					<c:when test="${empty requestScope.historyFive }">
						<tr>
							<td align="center" colspan="9">수강내역에 저장된 글이 없습니다.</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${requestScope.historyFive}" var="historyFive">
							<tr>
								<td align="center">${historyFive.edu_no }</td>
								<td align="center">${historyFive.edu_name }</td>
								<td align="center">${historyFive.edu_schedule }</td>
								<td align="center">${historyFive.edu_location }</td>

							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>

			</table>
		</div>
		<br>

		<div class="boxB">
			<div class="box3">
				<h5>
					<a href="${path}/instructor/main.do">강사</a>
				</h5>
				<hr>
				<table>
					<tr>
						<th align="center" style="width: 10%;">번호</th>
						<th align="center" style="width: 40%;">이름</th>
						<th align="center" style="width: 20%;">등록 일자</th>
					</tr>
					<c:choose>
						<c:when test="${empty requestScope.instructorFive }">
							<tr>
								<td align="center" colspan="9">강사가 등록된 글이 없습니다.</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${requestScope.instructorFive}"
								var="instructorFive">
								<tr>
									<td align="center">${instructorFive.instructor_no }</td>
									<td align="center">${instructorFive.name }</td>
									<td align="center">${instructorFive.hire_date }</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</table>
			</div>

			<div class="box4">
				<h5>
					<a href="${path}/PetitionList.do">청원</a>
				</h5>
				<hr>
				<table>
					<tr>
						<th align="center" style="width: 10%;">번호</th>
						<th align="center" style="width: 40%;">제목</th>
						<th align="center" style="width: 20%;">작성시간</th>
						<th align="center" style="width: 20%;">마감시간</th>
					</tr>

					<c:choose>
						<c:when test="${empty requestScope.petitionFive }">
							<tr>
								<td align="center" colspan="9">청원에 저장된 글이 없습니다.</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${requestScope.petitionFive}"
								var="petitionFive">
								<tr>
									<td align="center">${petitionFive.petition_no }</td>
									<td align="center">${petitionFive.title }</td>
									<td align="center">${petitionFive.write_time }</td>
									<td align="center">${petitionFive.closing_date }</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</table>
			</div>
		</div>
	</div>

</body>