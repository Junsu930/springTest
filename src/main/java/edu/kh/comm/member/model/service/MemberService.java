package edu.kh.comm.member.model.service;

import java.util.List;

import edu.kh.comm.member.model.vo.Member;

/* Service Interface를 사용하는 이유
 * 
 * 1. 프로젝트에 규칙성을 부여하기 위해서
 * 
 * 2. Spring AOP를 위해서 필요
 * 
 * cf) 흩어진 관심사 - 소스 코드 상에서 계속 반복해서 사용되는 부분, 유지보수를 어렵게 만든다. -> 모듈화하여 사용
 * 
 * 3. 클래스간의 결합도를 약화시키기 위해서 -> 유지보수성 향상
 * 
 * */ 
public interface MemberService {
	
	// 모든 메서드가 추상 메서드이다(묵시적으로 public abstract)
	
	// 모든 필드는 상수이다(묵시적으로 public static final)
	 
	/** 로그인 서비스
	 * @param inputMember
	 * @return loginMember
	 */
	public abstract Member login(Member inputMember);

	/** 이메일 중복 검사
	 * @param memberEmail
	 * @return result
	 */
	public abstract int emailDupCheck(String memberEmail);

	public abstract int nickDupCheck(String memberNickname);

	public abstract int signUp(Member inputMember);

	public abstract Member selectOne(String memberEmail);

	public abstract List selectAll();

}
