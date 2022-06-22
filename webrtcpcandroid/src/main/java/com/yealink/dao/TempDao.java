package com.yealink.dao;

import com.yealink.pojo.Temp;

import java.util.List;

/**
 *
 * @date 2018/3/16 15:35
 */
public interface TempDao {
    public Integer saveTemp(Temp temp);
    public Integer saveTempList(List<Temp> invited);
    public Temp getTemp(Temp temp);
}
