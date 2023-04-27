package edu.kh.comm.member.controller;


import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.comm.member.model.service.MyPageService;
import edu.kh.comm.member.model.vo.Member;

@Controller
@RequestMapping("/member/myPage")
@SessionAttributes({"loginMember"})
public class MyPageController {
	
	private Logger logger = LoggerFactory.getLogger(MyPageController.class);
	
	@Autowired
	private MyPageService service;
	// 비밀번호 변경
	
	// 회원 탈퇴
	
	@GetMapping("profile")
	public String profilePage() {
		
		return "member/myPage-profile";
		
	}
	@GetMapping("info")
	public String infoPage() {
		return "member/myPage-info";
	}
	
	@GetMapping("changePw")
	public String changePwPage() {
		return "member/myPage-changePw";
	}
	
	@GetMapping("secession")
	public String secessionPage() {
		return "member/myPage-secession";
	}
	
	@PostMapping("changePw")
	public String changePw(RedirectAttributes ra, @ModelAttribute("loginMember") Member loginMember ,@RequestParam("currentPw") String currentPw, @RequestParam("newPw") String newPw) {

		
		int result = service.changePw(loginMember, currentPw, newPw);
		
		if(result > 0) {
			ra.addFlashAttribute("msg", "비밀번호가 변경되었습니다.");
		}else {
			ra.addFlashAttribute("msg", "비밀번호가 변경에 실패하였습니다. 비밀번호를 확인해주세요");
		}
		
		
	
		return "redirect:/member/myPage/changePw";
	}
	
	@PostMapping("secession")
	public String secession(RedirectAttributes ra,SessionStatus status, @ModelAttribute("loginMember") Member loginMember, @RequestParam("memberPw") String memberPw) {
		
		int result = service.secession(loginMember, memberPw);
		
		if(result > 0) {
			// 세션 만료시킴
			status.setComplete();
			ra.addFlashAttribute("secessionMsg", "회원탈퇴가 완료되었습니다.");
			return "redirect:/";
		}else {
			ra.addFlashAttribute("secessionMsg", "회원탈퇴가 실패하였습니다. 비밀번호를 확인해주세요");
			return "redirect:/member/myPage/secession";
		}

	}
	
	
	//회원 정보 수정
	@PostMapping("/info")
	public String updateInfo(@ModelAttribute("loginMember") Member loginMember, 
			@RequestParam Map<String, Object> paramMap //요청시 전달된 파라미터를 구분하지 않고 모두 Map에 담아서 얻어옴
			, String[] updateAddress, RedirectAttributes ra
			) {
		
		// 필요한 값 
		// -닉네임
		// -전화번호
		// -주소(String[]로 얻어와서 String.join()을 이용해서 문자열로 변경) 
		// -회원 번호 (Session -> 로그인한 회원 정보를 통해서 얻어옴)
		// 			-> @ModleAttribute, @SessionAttributes 필요
		
		// @SessionAttributes의 역할 2가지
		// 1) Model에 세팅 데이터의 Key값을 @SessionAttributes에 작성하면
		//	해당 key값과 같은 Model에 세팅된 데이터를 request -> session scope로 이동
		
		// 2) 기존에 session scope로 이동시킨 값을 얻어오는 역할
		//	@ModelAttiribute("loginMember") Member loginMember 
		//					[session Key]
		// -> @SessionAttributes를 통해 session scope에 등록된 값을 얻어와
		// 	오른쪽에 작성된 Member loginMember 변수에 대입 
		//  단 @SessionAttributes가 클래스 위에 작성되어 있어야 가능
		
		// *** 매개변수를 이용해서 session, 파라미터 데이터를 동시에 얻어올 때 문제점 ***
		// -> @ModelAttribute("loginMember") Member loginMember
		
		// 파라미터의 name 속성값이 매개변수에 작성된 객체의 필드와 일치하면
		// -> name="memberNicknam"
		// session에 얻어온 객체의 필드에 덮어쓰기가 된다. 
		
		// [해결방법]
		// jsp에서 parameter로 받아올 name값의 이름을 변경해준다.
		
		// 닉네임 칸에 무언가를 적은 경우
		
		if(paramMap.get("updateNickname").equals(loginMember.getMemberNickname())) {
			loginMember.setMemberNickname((String)paramMap.get("updateNickname"));
		}else {
			// 닉네임에 새로운 값을 적은 경우
			String newNick = (String)paramMap.get("updateNickname");
			int checkNick = service.checkNick(newNick);
			
			// 만약 중복이 있다면
			if(checkNick > 0 ) {
				ra.addFlashAttribute("updateMsg", "회원정보 수정에 실패하였습니다.");
				return "redirect:/member/myPage/info";
			}else {
				loginMember.setMemberNickname(newNick);
			}
		}
		
		

		String address = String.join(",, ", updateAddress);
		loginMember.setMemberAddress(address);

		
		
		loginMember.setMemberTel((String)paramMap.get("updateTel"));

		
		int result = 0;
		
		result = service.updateInfo(loginMember);
		
		if(result > 0) {
			ra.addFlashAttribute("updateMsg", "회원정보 수정이 완료되었습니다.");
			return "redirect:/member/myPage/info";
		}else {
			ra.addFlashAttribute("updateMsg", "회원정보 수정에 실패하였습니다.");
			
			return "redirect:/member/myPage/info";
		}
	}
}

