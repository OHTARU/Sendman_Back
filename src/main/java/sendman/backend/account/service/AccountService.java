package sendman.backend.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sendman.backend.account.domain.Account;
import sendman.backend.account.dto.AccountinfoResponseDTO;
import sendman.backend.account.repository.AccountRepository;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.text.service.TextService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final TextService textService;

    public AccountinfoResponseDTO getUser(User user){
        Account account = findByAccount(user);
        return AccountinfoResponseDTO.builder()
                .email(account.getEmail())
                .name(account.getName()).build();
    }
    public Account findByAccount(User user){
        return accountRepository.findByEmail(user.getUsername()).orElseThrow(()->new UsernameNotFoundException("회원 정보 없음"));
    }
    public ResponseDTO deleteAccount(User user){
        Account account = findByAccount(user);
        textService.deleteTextAll(user);
        accountRepository.findById(account.getId());
        return new ResponseDTO("회원 탈퇴되었습니다.",null);
    }
}
