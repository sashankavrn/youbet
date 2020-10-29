package com.youbetcha.converter;

import com.youbetcha.model.ErrorDetails;
import com.youbetcha.model.dto.ErrorData;
import com.youbetcha.model.dto.PlayerRegistrationResponseDto;
import com.youbetcha.model.response.PlayerRegistrationResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RegistrationResponseToDtoConverter implements Converter<PlayerRegistrationResponse, PlayerRegistrationResponseDto> {

    @NonNull
    @Override
    public PlayerRegistrationResponseDto convert(PlayerRegistrationResponse source) {
        return PlayerRegistrationResponseDto.builder()
                .errorData(ErrorData.builder()
                        .errorCode(source.getErrorData().getErrorCode())
                        .errorDetails(source.getErrorData().getErrorDetails().stream().map(ErrorDetails::getErrorDetail).collect(Collectors.toList()))
                        .errorMessage(source.getErrorData().getErrorMessage())
                        .logId(source.getErrorData().getLogId())
                        .build())
                .success(source.getSuccess())
                .timestamp(source.getTimestamp())
                .userId(source.getUserId())
                .version(source.getVersion())
                .build();
    }
}
