package com.example.Cinematics.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//仓储接口
//**定义数据操作方法**：增删改查的各种方法
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByName(String name);
}
