package edu.kh.comm.board.model.service;

import java.util.List;

import edu.kh.comm.board.model.vo.Reply;

public interface ReplyService {

	/* 내가 짠 코드
	List<Reply> replyList(int boardNo);


	int replyInsert(String replyContent, int memberNo, int boardNo, int parentReplyNo);


	int replyUpdate(Reply reply);


	int replyDelete(int replyNo);
	*/

	/** 댓글목록 조회 서비스
	 * @param boardNo
	 * @return rList
	 */
	List<Reply> selectReplyList(int boardNo);

	/** 댓글 등록
	 * @param reply
	 * @return result
	 */
	int insertReply(Reply reply);
	

	/** 댓글 수정
	 * @param reply
	 * @return result
	 */
	int replyUpdate(Reply reply);


	/** 댓글 삭제
	 * @param replyNo
	 * @return result
	 */
	int replyDelete(int replyNo);

}
