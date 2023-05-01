package edu.kh.comm.member.model.service;


import java.io.IOException;
import java.util.Map;

import edu.kh.comm.member.model.vo.Member;

public interface MyPageService {

	int changePw(Member loginMember, String currentPw, String newPw);

	int secession(Member loginMember, String memberPw);

	int updateInfo(Member loginMember);

	int checkNick(String newNick);

	int updateProfile(Map<String, Object> map) throws IOException;

}
