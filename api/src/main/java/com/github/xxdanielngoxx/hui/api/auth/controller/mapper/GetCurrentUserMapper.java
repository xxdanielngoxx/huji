package com.github.xxdanielngoxx.hui.api.auth.controller.mapper;

import com.github.xxdanielngoxx.hui.api.auth.controller.response.UserResource;
import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GetCurrentUserMapper {
  GetCurrentUserMapper INSTANCE = Mappers.getMapper(GetCurrentUserMapper.class);

  UserResource entityToResource(UserEntity userEntity);
}
