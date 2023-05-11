package edu.kh.comm.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.comm.board.model.dao.ReplyDAO;
import edu.kh.comm.board.model.vo.Reply;
import edu.kh.comm.common.Util;

@Service
public class ReplyServiceImpl implements ReplyService {
	
	@Autowired
	private ReplyDAO dao;
	
	/* 내가 짠 코드

	@Override
	public List<Reply> replyList(int boardNo) {
		
		return dao.replyList(boardNo);
	}



	@Override
	public int replyInsert(String replyContent, int memberNo, int boardNo, int parentReplyNo) {
		
		Reply reply = new Reply();
		
		reply.setBoardNo(boardNo);
		reply.setReplyContent(replyContent);
		reply.setMemberNo(memberNo);
		reply.setParentReplyNo(parentReplyNo);
		
		return dao.replyInsert(reply);
	}



	@Override
	public int replyUpdate(Reply reply) {
		
		
		return dao.replyUpdate(reply);
	}



	@Override
	public int replyDelete(int replyNo) {
		return  dao.replyDelete(replyNo);
	}

	*/
	// 댓글 목록 조회
	@Override
	public List<Reply> selectReplyList(int boardNo) {
		
		return dao.selectReplyList(boardNo);
	}

	// 댓글 등록 
	@Override
	public int insertReply(Reply reply) {
		
		System.out.println(reply.toString());
		
		if(reply.getParentReplyNo() != 0) { // 답글일 경우
			String parentNick = dao.selectParentNick(reply.getParentReplyNo());
			System.out.println("패닉: " + parentNick);
			reply.setParentNick(parentNick);
		}
		
		
		
		//xss, 개행문자 처리
		reply.setReplyContent(Util.XSSHandling(reply.getReplyContent()));
		reply.setReplyContent(Util.newLineHandling(reply.getReplyContent()));
		
		
		return dao.insertReply(reply);
	}
	
	@Override
	public int replyUpdate(Reply reply) {
		
		
		return dao.replyUpdate(reply);
	}



	@Override
	public int replyDelete(int replyNo) {
		return  dao.replyDelete(replyNo);
	}


}
