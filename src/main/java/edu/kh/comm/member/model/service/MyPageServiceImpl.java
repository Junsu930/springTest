package edu.kh.comm.member.model.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.comm.common.Util;
import edu.kh.comm.member.model.dao.MyPageDAO;
import edu.kh.comm.member.model.vo.Member;

@Service
public class MyPageServiceImpl implements MyPageService {
	
	@Autowired
	private MyPageDAO dao;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Override
	public int changePw(Member loginMember ,String currentPw, String newPw) {
		
		String dbPw = dao.checkPw(loginMember);
		
		int result = 0;
		
		if(bcrypt.matches(currentPw, dbPw)) {
			loginMember.setMemberPw(bcrypt.encode(newPw));
			result = dao.changePw(loginMember);
			
		}else{
			return -1;
		}
		
		return result;
	}

	@Override
	public int secession(Member loginMember, String memberPw) {
		
		String dbPw = dao.checkPw(loginMember);
		
		int result = 0;
		
		if(bcrypt.matches(memberPw, dbPw)){
			
			result = dao.secession(loginMember);
			
		}else {
			return -1;
		}
		
		return result;
	}

	@Override
	public int updateInfo(Member loginMember) {
		return dao.updateInfo(loginMember);
	}

	@Override
	public int checkNick(String newNick) {
		return dao.checkNick(newNick);
	}

	// 프로필 이미지 수정 서비스 구현
	@Override
	public int updateProfile(Map<String, Object> map)  throws IOException{
			// webPath, folderPath, uploadImage, delete, memberNo
		
		// 자주 쓰는 값 변수에 저장

	MultipartFile uploadImage = (MultipartFile)map.get("uploadImage");
	String delete = (String)map.get("delete"); // "0" 변경 / "1" 삭제

	// 프로필 이미지 삭제여부를 확인해서
	// 삭제가 아닌 경우 (== 새 이미지로 변경) -> 업로드된 파일명을 변경 
	// 삭제가 된 경우 -> Null 값을 준비(DB upload)
	
	String renameImage = null; // 변경된 파일명 저장
	
	if(delete.equals("0")) { // 변경인 경우
		
		// 파일명 변경
		// uploadImage.getOriginalFilename() : 원본 파일명
		renameImage = Util.fileRename(uploadImage.getOriginalFilename());
		// 234903284903.png 
		
		map.put("profileImage", map.get("webPath") + renameImage);
		
	}else { // 삭제인 경우
		map.put("profileImage", null);
	}
	
	// DAO를 호출해서 프로필 이미지 수정
	int result = dao.updateProfile(map);
		
	
	// DB수정 성공 시 메모리에 임시저장 되어있는 파일을 서버에 저장
	if(result>0 && map.get("profileImage") != null) {
		uploadImage.transferTo(new File(map.get("folderPath") + renameImage));
	}
	
	return result;
	}

}
