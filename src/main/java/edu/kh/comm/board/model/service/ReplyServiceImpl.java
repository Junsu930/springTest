package edu.kh.comm.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.comm.board.model.dao.ReplyDAO;
import edu.kh.comm.board.model.vo.Reply;

@Service
public class ReplyServiceImpl implements ReplyService {
	
	@Autowired
	private ReplyDAO dao;

	/** 댓글목록 반환 메서드
	 * @return list
	 */
	@Override
	public List<Reply> replyList(int boardNo) {
		
		return dao.replyList(boardNo);
	}


	/** 댓글 작성 메서드
	 *
	 */
	@Override
	public int replyInsert(String replyContent, int memberNo, int boardNo, int parentReplyNo) {
		
		Reply reply = new Reply();
		
		reply.setBoardNo(boardNo);
		reply.setReplyContent(replyContent);
		reply.setMemberNo(memberNo);
		reply.setParentReplyNo(parentReplyNo);
		
		return dao.replyInsert(reply);
	}


	/** 댓글수정 매서드
	 *
	 */
	@Override
	public int replyUpdate(Reply reply) {
		
		
		return dao.replyUpdate(reply);
	}


	/** 댓글 삭제 메서드
	 *
	 */
	@Override
	public int replyDelete(int replyNo) {
		return  dao.replyDelete(replyNo);
	}

}
