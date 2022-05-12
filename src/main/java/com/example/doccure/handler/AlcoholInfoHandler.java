package com.example.doccure.handler;

import com.example.doccure.entity.health_basicInfo.AlcoholInfo;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(AlcoholInfo.class)
public class AlcoholInfoHandler extends AbstractObjectTypeHandler<AlcoholInfo>{
}
