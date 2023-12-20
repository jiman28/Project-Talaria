package com.noelwon.model.ccc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.noelwon.model.userDto.User;

@Repository
public interface BoardEntityRepository extends JpaRepository<BoardEntity, Integer>{
	@Modifying
	@Query(value = "update board_entity b set b.views = b.views + 1 where b.article_no = :article_no", nativeQuery = true)
	void updateViews(@Param("article_no")int article_no);
	
	@Query(value = "select " + "* " + "from board_entity b " + "where " + "b.title like concat('%',?1,'%')", nativeQuery = true)
	public Page<BoardEntity> findAllBytitleKeyword(
			@Param("kw") String kw, Pageable pageable);

	@Query(value = "select " + "* " + "from board_entity b " + "where " + "b.content like concat('%',?1,'%')", nativeQuery = true)
	public Page<BoardEntity> findAllBycontentKeyword(
			@Param("kw") String kw, Pageable pageable);

	@Query(value = "select " + "* " + "from board_entity b " + "where " + "b.write_id like concat('%',?1,'%')", nativeQuery = true)
	public Page<BoardEntity> findAllBywriterKeyword(
			@Param("kw") String kw, Pageable pageable);
	
	public List<BoardEntity> findByuser (User user);

}
