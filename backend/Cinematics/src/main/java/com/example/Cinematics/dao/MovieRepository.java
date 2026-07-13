package com.example.Cinematics.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
//仓储接口
//**定义数据操作方法**：增删改查的各种方法
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByName(String name);

    // 用户操作：多条件查询电影信息
    @Query("SELECT m FROM Movie m WHERE " +
            "(:name IS NULL OR m.name LIKE CONCAT('%', :name, '%')) AND " +
            "(:time IS NULL OR m.time = :time) AND " +
            "(:nation IS NULL OR m.nation = :nation)")
    List<Movie> searchMovies(@Param("name") String name,
                             @Param("time") Integer time,
                             @Param("nation") String nation);
}
