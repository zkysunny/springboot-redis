package com.jason.dao;


import com.jason.entity.Person;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by jason on 2017/12/25.
 */
@Mapper
public interface PersonMapper {
    @Select("SELECT * FROM person WHERE NAME = #{name}")
   public Person SelectPerson(@Param("name") String name);


}
