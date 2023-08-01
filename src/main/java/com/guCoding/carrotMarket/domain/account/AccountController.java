package com.guCoding.carrotMarket.domain.account;

import com.guCoding.carrotMarket.config.auth.LoginUser;
import com.guCoding.carrotMarket.dto.ResponseDto;
import com.guCoding.carrotMarket.dto.account.AccountReqDto;
import com.guCoding.carrotMarket.dto.account.AccountReqDto.AccountChargeReqDto;
import com.guCoding.carrotMarket.dto.account.AccountReqDto.AccountSaveReqDto;
import com.guCoding.carrotMarket.dto.account.AccountReqDto.AccountTransferReqDto;
import com.guCoding.carrotMarket.dto.account.AccountRespDto;
import com.guCoding.carrotMarket.dto.account.AccountRespDto.AccountChargeRespDto;
import com.guCoding.carrotMarket.dto.account.AccountRespDto.AccountSaveRespDto;
import com.guCoding.carrotMarket.dto.account.AccountRespDto.AccountTransferRespDto;
import com.guCoding.carrotMarket.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;
    
    @PostMapping("/api/s/account")
    public ResponseEntity<?> saveAccount(@RequestBody @Valid AccountSaveReqDto accountSaveReqDto,
                                         BindingResult bindingResult, @AuthenticationPrincipal LoginUser loginUser){
        AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(accountSaveReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌등록성공", accountSaveRespDto), HttpStatus.OK);
    }

    @PostMapping("/api/s/charge")
    public ResponseEntity<?> chargeAccount(@RequestBody @Valid AccountChargeReqDto accountChargeReqDto, BindingResult bindingResult){
        AccountChargeRespDto accountChargeRespDto = accountService.계좌충전(accountChargeReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌충전성공", accountChargeRespDto), HttpStatus.OK);
    }

    @PostMapping("/api/s/transfer")
    public ResponseEntity<?> transferAccount(@RequestBody @Valid AccountTransferReqDto accountTransferReqDto,
                                             BindingResult bindingResult, @AuthenticationPrincipal LoginUser loginUser){
        AccountTransferRespDto accountTransferRespDto = accountService.판매하기(accountTransferReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "판매하기성공", accountTransferRespDto), HttpStatus.OK);
    }
}
