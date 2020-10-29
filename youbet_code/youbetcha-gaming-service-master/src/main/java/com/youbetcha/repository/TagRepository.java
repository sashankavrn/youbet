package com.youbetcha.repository;

import com.youbetcha.model.entity.Game;
import com.youbetcha.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {}
