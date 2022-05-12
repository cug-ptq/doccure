package com.example.doccure.handler;

import com.example.doccure.entity.health_basicInfo.NormalHealthyInfo;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(NormalHealthyInfo.class)
public class NormalInfoHandler extends AbstractObjectTypeHandler<NormalHealthyInfo>{
}
