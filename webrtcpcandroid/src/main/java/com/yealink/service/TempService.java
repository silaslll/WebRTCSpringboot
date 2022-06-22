package com.yealink.service;

import com.yealink.pojo.Temp;

import java.util.List;

/**
 *
 * @date 2018/3/19 10:50
 */
public interface TempService {
    public Integer saveTempList(List<Temp> invited);
    public boolean userCanJoin(Temp temp);
}
