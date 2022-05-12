package com.example.doccure.handler;

import com.example.doccure.entity.health_basicInfo.SmokeInfo;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes({JdbcType.VARCHAR})
@MappedTypes({SmokeInfo.class})
public class SmokeInfoHandler extends AbstractObjectTypeHandler<SmokeInfo>{
}
