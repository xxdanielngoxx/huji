package com.github.xxdanielngoxx.hui.api.owner.controller.mapper;

import com.github.xxdanielngoxx.hui.api.owner.controller.request.RegisterOwnerRequest;
import com.github.xxdanielngoxx.hui.api.owner.service.command.RegisterOwnerCommand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegisterOwnerMapper {

  RegisterOwnerMapper INSTANCE = Mappers.getMapper(RegisterOwnerMapper.class);

  RegisterOwnerCommand requestToCommand(RegisterOwnerRequest request);
}
