package com.example.doccure.handler;

import com.example.doccure.entity.health_basicInfo.PhysicalInfo;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(PhysicalInfo.class)
public class PhysicalInfoHandler extends AbstractObjectTypeHandler<PhysicalInfo>{
}
