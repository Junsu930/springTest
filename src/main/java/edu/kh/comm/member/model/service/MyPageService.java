package edu.kh.comm.member.model.service;


import edu.kh.comm.member.model.vo.Member;

public interface MyPageService {

	int changePw(Member loginMember, String currentPw, String newPw);

	int secession(Member loginMember, String memberPw);

	int updateInfo(Member loginMember);

	int checkNick(String newNick);

}
