package com.kh.finalproject.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dto.BoardDto;
import com.kh.finalproject.dto.BoardInviteDto;
import com.kh.finalproject.dto.InviteViewDto;
import com.kh.finalproject.error.TargetNotFoundException;
import com.kh.finalproject.service.TokenService;
import com.kh.finalproject.vo.ClaimVO;

@CrossOrigin
@RestController
@RequestMapping("/api/board")
public class BoardRestController {

	@Autowired
	private BoardDao boardDao;
	@Autowired
	private TokenService tokenService;
	
	//보드 생성
	@PostMapping("/")
	public long create(@RequestBody BoardDto boardDto, @RequestHeader("Authorization") String accessToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = claimVO.getUserNo();
		boardDto.setAccountNo(accountNo);
		long boardNo = boardDao.createBoard(boardDto);
		
		//보드 만들고 입장처리
		boardDao.enterBoard(boardNo, accountNo);
		return boardNo;
	}
	
	//유저의 보드 리스트 조회
	@GetMapping("/")
	public List<BoardDto> list(@RequestHeader("Authorization") String accessToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = claimVO.getUserNo();
		List<BoardDto> result = boardDao.selectBoardList(accountNo);
		return result;
	}
	
	//보드 no로 보드 상세 조회
	@GetMapping("/{boardNo}")
	public BoardDto find(@PathVariable long boardNo) {
		return boardDao.selectOne(boardNo);
	}
	
	//보드 삭제
	@DeleteMapping("/{boardNo}")
	public void delete(@PathVariable long boardNo, @RequestHeader("Authorization") String accessToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = claimVO.getUserNo();
		BoardDto boardDto = boardDao.selectOne(boardNo);
		if(boardDto == null)
			throw new TargetNotFoundException("존재하지 않는 보드");
		if(boardDto.getAccountNo() != accountNo)
			throw new TargetNotFoundException("소유자 불일치");
		
		boardDao.deleteBoard(boardNo);
	}
	
	//보드에 입장하는
	@PostMapping("/enter/{boardNo}")
	public void enter(@PathVariable long boardNo, @RequestHeader("Authorization") String accessToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = claimVO.getUserNo();
		BoardDto boardDto = boardDao.selectOne(boardNo);
		if(boardDto == null)
			throw new TargetNotFoundException("존재하지 않는 보드");
		
		//보드의 멤버인지 확인
		boolean isMember = boardDao.selectBoardMember(boardNo, accountNo);
		if(isMember == false) {
			boardDao.enterBoard(boardNo, accountNo);
		}
	}
	
}
