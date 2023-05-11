package edu.kh.comm.board.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.board.model.vo.Reply;

@Repository
public class ReplyDAO {

	@Autowired
	private  SqlSessionTemplate sqlSession;

	/* 내가 짠 코드
	public List<Reply> replyList(int boardNo) {
		return sqlSession.selectList("replyMapper.selectReply", boardNo);
	}


	public int replyInsert(Reply reply) {

		return sqlSession.insert("replyMapper.insertReply",reply);
	}


	public int replyUpdate(Reply reply) {
		return sqlSession.update("replyMapper.updateReply", reply);
	}

	public int replyDelete(int replyNo) {
		return sqlSession.update("replyMapper.deleteReply", replyNo);
	}
	*/

	/** 댓글 목록 조회
	 * @param boardNo
	 * @return rList
	 */
	public List<Reply> selectReplyList(int boardNo) {
		return sqlSession.selectList("replyMapper.selectReplyList", boardNo);
	}

	/** 댓글 등록 DAO
	 * @param reply
	 * @return result
	 */
	public int insertReply(Reply reply) {
		return sqlSession.insert("replyMapper.insertReply", reply);
	}
	
	/** 댓글 수정
	 * @param reply
	 * @return result
	 */
	public int replyUpdate(Reply reply) {
		return sqlSession.update("replyMapper.updateReply", reply);
	}

	/** 댓글 삭제
	 * @param replyNo
	 * @return result
	 */
	public int replyDelete(int replyNo) {
		return sqlSession.update("replyMapper.deleteReply", replyNo);
	}

	/** 부모댓글 닉네임 가져오기
	 * @param parentReplyNo
	 * @return
	 */
	public String selectParentNick(int parentReplyNo) {
		return sqlSession.selectOne("replyMapper.selectParentNick", parentReplyNo);
	}

}
