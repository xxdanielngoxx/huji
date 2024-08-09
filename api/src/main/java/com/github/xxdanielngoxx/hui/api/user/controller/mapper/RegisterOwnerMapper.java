package com.github.xxdanielngoxx.hui.api.user.controller.mapper;

import com.github.xxdanielngoxx.hui.api.user.controller.request.RegisterOwnerRequest;
import com.github.xxdanielngoxx.hui.api.user.service.command.RegisterOwnerCommand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegisterOwnerMapper {

  RegisterOwnerMapper INSTANCE = Mappers.getMapper(RegisterOwnerMapper.class);

  RegisterOwnerCommand requestToCommand(RegisterOwnerRequest request);
}
