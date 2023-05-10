package edu.kh.comm.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.comm.board.model.service.ReplyService;
import edu.kh.comm.board.model.vo.Reply;

// REST (REpresentational State Transfer) : 
// - 자원을 이름으로 구분(REpresntational, 자원의 표현)하여
// 자원의 상태(State)를 주고받는 것(Transfer)

// -> 특정한 이름(주소)로 요청이 오면 값으로 응답

// @RestController : 요청에 따른 응답이 모두 데이터(값) 자체인 컨트롤러
// -> @Controller + @ResponseBody

@RestController
@RequestMapping("/reply")
public class ReplyController {
	
	@Autowired
	private ReplyService service;
	
	// 댓글 목록 조회
	@GetMapping("/selectReplyList")
	public List<Reply> replyList(int boardNo){
		
		List<Reply> replyList = new ArrayList<>();
		
		replyList = service.replyList(boardNo);
		
		return replyList;
	}
	
//	// 댓글 등록
//	@PostMapping("/insert")
//	public int replyInsert(String replyContent, int memberNo, int boardNo) {
//		int result = 0;
//		
//		result = service.replyInsert(replyContent, memberNo, boardNo);
//		
//		
//		return result;
//	}
	
	// 댓글 등록
	@PostMapping("/insert")
	public int replyInsert(String replyContent, int memberNo, int boardNo,
			@RequestParam(value="parentReplyNo", required=false, defaultValue = "-1") int parentReplyNo ){
		int result = 0;
		
		
		
		result = service.replyInsert(replyContent, memberNo, boardNo, parentReplyNo);
		
		
		return result;
	}
	// 댓글 수정
	@PostMapping("/update")
	public int replyUpdate(Reply reply) {
		int result=0;
		
		result = service.replyUpdate(reply);
		
		return result;
	}
	
	// 댓글 삭제
	@GetMapping("/delete")
	public int replyDelete(int replyNo) {
		
		int result=0;
		
		result = service.replyDelete(replyNo);
		
		System.out.println(result);
		
		return result;
	}

}