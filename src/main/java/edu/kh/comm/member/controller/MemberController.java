package edu.kh.comm.member.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import edu.kh.comm.member.model.service.MemberService;
import edu.kh.comm.member.model.vo.Member;

// POJO 기반 프레임워크 : 외부 라이브러리 상속 X 

// class : 객체를 만들기 위한 설계도 
// -> 객체로 생성되어야 기능 수행 가능하다.
// -> IOC(제어의 역전, 객체의 생명주기를 스프링이 관리)를 이용하여 객체 생성
// ** 이 때 스프링이 생성한 객체를 bean이라고 한다. ** 

// bean 등록 == 스프링이 객체로 만들어서 가지고 있어라

// @Component // 해당 클래스를 bean으로 등록하라고 프로그램에게 알려주는 주석(Annotation)


@Controller // 생성된 bean이 Controller임을 명시 + bean 등록
@RequestMapping("/member") // localhost:8080/comm/member 이하의 요청을 처리하는 컨트롤러
@SessionAttributes({"loginMember"}) // Model에 추가된 값의 Key와 어노테이션에 작성된 값이 같으면 해당 값을 session scope로 이동시키는 역할
public class MemberController {
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired  // bean으로 등록된 객체 중 타입이 같거나, 상속관계인 bean을 주입해주는 역할 // DI(의존성 주입)
	private MemberService service; // = new MemberServiceImpl(); // IOC 제어의 역전 위반으로 new 연산자를 통해서 개발자가 객체생성하지 않는다
	
	// Controller : 요청/응답을 제어하는 역할을 하는 클래스 
	
	/* @RequestMapping : 클라이언트 요청(url)에 맞는 클래스 or 메서드를 연결시켜주는 어노테이션
	 * 
	 * [ 위치에 따른 해석 ]
	 * - 클래스 레벨 : 공통 주소
	 * - 메서드 레벨 : 공통 주소 외 나머지 주소
	 * 
	 * 단, 클래스 레벨에 @RequestMapping이 존재하지 않는다면
	 * - 메서드 레벨 : 단독 요청 처리 주소
	 * 
	 * [ 작성법에 따른 해석 ]
	 * 
	 * 1) @RequestMapping("url")
	 * --> 요청 방식(GET/POST) 관계 없이 url이 일치하는 요청 처리
	 * 
	 * 2) @RequestMapping(value="url", method="RequestMethod.GET | POST")
	 * --> 요청 방식에 따라 요청 처리함
	 *
	 * ** 메서드 레벨에서 GET/POST 방식을 구분하여 매핑할 경우 **
	 * @GetMapping("url") / @PostMapping("url") 사용하는 것이 일반적
	 * (메서드 레벨에서만 작성 가능!)
	 *  
	 * */
	
	/* 만약 클래스 레벨에 @RequestMapping("/member")가 없으면
	 * localhost:8080/comm/login 으로 매핑됨
	 * */
	
	// 로그인
	// 요청 시 파라미터를 얻어오는 방법 1 
	// -> HttpServletRequest 이용
	
//	@RequestMapping("/login") // localhost:8080/comm/member/login
//	public String login(HttpServletRequest req) {
//		logger.info("로그인 요청됨");
//		
//		String inputEmail = req.getParameter("inputEmail");
//		String inputPw = req.getParameter("inputPw");
//		
//		logger.debug("inputEmail : "+ inputEmail);
//		logger.debug("inputPw : "+ inputPw);
//		
//		
//		return "redirect:/"; // sendRedirect 안 써도 된다. 이게 리다이렉트 구문임
//	}
	
	// 요청 시 파라미터를 얻어오는 방법 2 
	// -> @RequestParam 어노테이션 사용
	
	// @RequestParam("name속성값") 자료형 변수명
	// - 클라이언트 요청 시 같이 전달된 파라미터를 변수에 저장 
	// --> 어떤 파라미터를 변수에 저장할지는 "name속성값"을 이용해 지정
	
	// 매개변수 지정 시 데이터 타입 파싱을 자유롭게 진행할 수 있음 ex) String -> int로 변환
	
	// [속성]
	// value : input 태그의 name 속성값
	
	// required : 입력된 name 속성 값이 필수적으로 파라미터에 포함되어야 하는지를 지정
	// 			required = true/false (기본값 true)
	
	// defaultValue : required가 false인 상태에서 파라미터가 존재하지 않을 경우의 값을 지정
	
//	@RequestMapping("login")
//	public String login(@RequestParam("inputEmail") String inputEmail,
//			@RequestParam("inputPw") String inputPw,
//			@RequestParam(value="inputName", required=false, defaultValue="홍홍이") String inputName) {
//		
//		logger.debug("inputEmail : "+ inputEmail);
//		logger.debug("inputPw : "+ inputPw);
//		logger.debug("inputName : "+ inputName);
//		
//		
//		// email을 숫자만 입력받는다고 가정
//		// logger.debug(inputEmail + 100);
//		
//		return "redirect:/";
//	}
	
	// 요청 시 파라미터를 얻어오는 방법 3
	// -> @ModelAttribute 어노테이션 사용
	
	// @ModelAttribute VO타입 변수명
	// -> 파라미터 중 name 속성 값이 VO의 필드와 일치하면
	// 해당 VO 객체의 필드에 값을 세팅
	
	// *** @ModelAttribute를 이용해서 객체에 값을 직접 담는 경우에 대한 주의 사항 ***
	// - 반드시 필요한 내용
	// - VO 기본 생성자
	// - VO 필드에 대한 Setter
	
	
	@PostMapping("/login")
	public String login(@ModelAttribute Member inputMember, 
			Model model,
			RedirectAttributes ra,
			HttpServletResponse resp,
			HttpServletRequest req,
			@RequestParam(value="saveId", required=false) String saveId
			) {
		
		// 커맨드 객체
		// @ModelAttribute 생략된 상태에서 파라미터가 필드에 세팅된 객체
		
		/* Model : 데이터를 맵 형식(K:V) 형태로 담아 전달하는 용도의 객체
		 * -> request, session을 대체하는 객체
		 * 
		 * - 기본 scope : request
		 * - session scope로 변환하고 싶은 경우
		 * 클래스 레벨로 @SessionAttributes를 작성하면 된다.
		 * 
		 * @SessionAttributes 미작성 -> request scope
		 * */
		
		logger.info("로그인 기능 수행됨");
		
		
		// 아이디, 비밀번호가 일치하는 회원정보를 조회하는 Service 호출 후 결과 반환 받기
		Member loginMember = service.login(inputMember);
		
		if(loginMember!=null) { //로그인 성공 시
			model.addAttribute("loginMember", loginMember); // == req.setAttributes("loginMember", loginMember);
			// 로그인 성공 시 무조건 쿠키 생성
			// 단, 아이디 저장 체크 여부에 따라서 쿠키 시간 
			
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			
			if(saveId != null) { // 아이디 저장 체크되었을 때
				
				cookie.setMaxAge(60 * 60 * 24*  365); //초단위 지정(1년)
				
			}else {
				
				cookie.setMaxAge(0);
			}
			// 쿠키가 적용될 범위 지정
			cookie.setPath(req.getContextPath());
			// 최상위 /comm 이후 모두 적용됨
			
			// 쿠키를 응답 시 클라이언트에게 전달
			resp.addCookie(cookie);
			
		}else { //로그인 실패 시
			// model.addAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		
		// redirect 시에도 request scope로 세팅된 데이터가 유지될 수 있도록 하는 방법을
		// spring에서 제공해줌
		// -> RedirectAttributes 객체 (컨트롤러 매개변수에 작성하면 사용 가능)
		
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(/*HttpSession session*/ SessionStatus status) {
		// 로그아웃 == 세션을 없애는 것
		logger.info("로그아웃 수행됨");
		
		// * @SessionAttributes를 이용해서 session scope에 배치된 데이터는
		// SessionStatus라는 별도 객체를 이용해야만 없앨 수 있다.
		
		// session.invalidate(); 기존 세션 무효화 방식으로는 X
		
		status.setComplete(); // 세션이 할 일이 완료됨 -> 없앰
		
		return "redirect:/"; // 메인 페이지 재요청
	}
	
	// 회원 가입 화면 전환
	@GetMapping("/signUp") // Get 방식 : /comm/member/signUp 요청
	public String signUp() {
		
		return "member/signUp"; 
	}
	
	// 이메일 중복 검사
	@GetMapping("/emailDupCheck")
	@ResponseBody // ajax 응답 시 사용
	//public String emailDupCheck(@RequestParam("memberEmail") String memberEmail) { // 파라미터 key값과 저장하려는 변수 명이 같으면 생략 가능
	public int emailDupCheck( String memberEmail ) {
		int result = service.emailDupCheck(memberEmail);
		
		// 컨트롤러에서 반환되는 값은 forward 또는 redirect를 위한 경로인 경우가 일반적
		// -> 반환되는 값은 경로로 인식됨
		
		// -> 이를 해결하기 위한 어노테이션 @ResponseBody가 존재함 
		
		// @ResponseBody : 반환되는 값을 응답은 몸통(body)에 추가하여 이전 주소로 돌아감
		// -> 컨트롤러에서 반환되는 값이 경로가 아닌 "값 자체"로 인식됨
		
		
		return result;
	}
	
	@GetMapping("/nicknameDupCheck")
	@ResponseBody
	public int nickDupCheck(String memberNickname) {
		return service.nickDupCheck(memberNickname);
	}
	
	@PostMapping("/signUp")
	public String signUp(@ModelAttribute Member inputMember, 
			RedirectAttributes rs, String[] memberAddress) {
	
		
		inputMember.setMemberAddress(String.join(",,", memberAddress));
		
		if(inputMember.getMemberAddress().equals(",,,,")||inputMember.getMemberAddress().equals(",,,")) {
			inputMember.setMemberAddress(null); // null로 변환
		}
		
		int result = service.signUp(inputMember);
		
		if(result > 0) {
			rs.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
			return "redirect:/";

		}
		else {
			rs.addFlashAttribute("msg", "회원가입이 실패하였습니다.");
			return "redirect:/member/signUp";
		}
	}
	
	@PostMapping("/selectOne")
	@ResponseBody
	public Member selectOne(String memberEmail) {
		return service.selectOne(memberEmail);
	}
	
	@RequestMapping("/selectAll")
	@ResponseBody
	public String selectAll() {
		return new Gson().toJson(service.selectAll());
	}
	
	
	/* 스프링 예외 처리 방법 (3가지, 중복 사용 가능)
	 * 
	 * 1 순위 : 메서드 별로 예외처리 (try ~ catch / throws )
	 * 
	 * 2 순위 : 하나의 컨트롤러에서 발생하는 예외를 모아서 처리
	 * 			-> @ExceptionHandler (메서드에 작성)
	 * 3 순위 : 전역(웹 애플리케이션)에서 발생하는 예외를 모아서 처리
	 *  		-> @ControllerAdvice (클래스에 작성)
	 * */
	
	// 회원 컨트롤러에서 발생하는 모든 예외를 모아서 처리
//	@ExceptionHandler(Exception.class)
//	public String exceptionHandler(Exception e, Model model) {
//		e.printStackTrace();
//		
//		model.addAttribute("errorMessage", "서비스 이용 중 문제가 발생했습니다.");
//		
//		model.addAttribute("e", e);
//		
//		return "common/error";
//	}
	
}
