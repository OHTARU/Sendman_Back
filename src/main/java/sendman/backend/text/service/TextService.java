package sendman.backend.text.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sendman.backend.account.domain.Account;
import sendman.backend.account.service.AccountService;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.text.domain.SaveType;
import sendman.backend.text.domain.Text;
import sendman.backend.text.dto.AiResponseDTO;
import sendman.backend.text.dto.TextResponseDTO;
import sendman.backend.text.repository.TextRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TextService {
    private final TextRepository textRepository;
    private final AccountService accountService;
    @Value("${file.dir}")
    private String fileDir;

    public ResponseDTO saveVoice(User resUser, MultipartFile file) throws IOException, InterruptedException, UsernameNotFoundException {
        //AI API 통신 후 백업
        String text = AIGetText(file,SaveType.STT);

        // 만료기간 설정 ( 7일 )
        LocalDate exp = LocalDate.now().plusWeeks(1);

        //db 저장
        Account account = accountService.findByAccount(resUser);
        textRepository.save(Text.builder()
                .account(account)
                .exp(exp)
                .text(text)
                .type(SaveType.STT)
                .build());

        return new ResponseDTO("저장되었습니다", new AiResponseDTO(text));
    }

    public ResponseDTO saveImage(User resUser, MultipartFile file) throws IOException, InterruptedException, UsernameNotFoundException {
        //AI API 통신 후 백업
        String text = AIGetText(file,SaveType.TTS);

        // 만료기간 설정 ( 7일 )
        LocalDate exp = LocalDate.now().plusWeeks(1);

        //DB 저장
        Account account = accountService.findByAccount(resUser);
        textRepository.save(Text.builder()
                .text(text)
                .exp(exp)
                .account(account)
                .type(SaveType.TTS)
                .build());

        return new ResponseDTO("저장 되었습니다.",new AiResponseDTO(text));
    }

    //매일 0시 5분마다 데이터 삭제
    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
    public void clearImageData() throws IOException {
        List<Text> textsData = textRepository.findByExp(LocalDate.now());
        textRepository.deleteAll(textsData);
    }

    //paging 처리 5개씩 데이터 전송
    public ResponseDTO getList(User user,int pageNum) throws UsernameNotFoundException {
        Account account = accountService.findByAccount(user);
        Pageable pageable = PageRequest.of(pageNum, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<TextResponseDTO> textPage = textRepository.findByAccount(account,pageable).map(TextResponseDTO::new);

        return new ResponseDTO(pageNum + "번째 페이지 입니다.", textPage);
    }

    //detail Text 데이터 전송
    public ResponseDTO getDetail(Long id) throws ChangeSetPersister.NotFoundException {
        TextResponseDTO textResponseDTO = textRepository.findById(id).map(TextResponseDTO::new)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        return new ResponseDTO(null, textResponseDTO);
    }

    public ResponseDTO deleteText(List<Long> id){
        try {
            for (Long i : id){
                textRepository.deleteById(i);
            }
            return new ResponseDTO("Text가 삭제 되었습니다.",null);
        }catch (EmptyResultDataAccessException e){
            return new ResponseDTO("오류가 발생되었습니다", e);
        }
    }

    public ResponseDTO deleteTextAll(User user){
        textRepository.deleteAllByAccount(accountService.findByAccount(user));
        return new ResponseDTO("삭제 되었습니다",null);
    }

    private String AIGetText(MultipartFile file, SaveType type) throws IOException, InterruptedException {
        String origName = file.getOriginalFilename();

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        // uuid와 확장자 결합
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir+type+"/" + savedName;

        file.transferTo(new File(savedPath));

        //AI 실행
        ProcessBuilder builder;
        if (SaveType.STT==type){
            builder = new ProcessBuilder(
                    "src/main/resources/model/venv/bin/python3", "src/main/resources/model/Ko_STT.py",
                    "--audio_path", savedPath);
        }else{
            builder = new ProcessBuilder(
                    "src/main/resources/model/venv/bin/python3", "src/main/resources/model/ocr.py",
                    "--image_path", savedPath);
        }
        Process process = builder.start();

        InputStreamReader inputStream = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStream);

        StringBuilder sb = new StringBuilder();
        int ch;
        System.out.println("reader : ");
        while ((ch=reader.read())!=-1) {
            // 실행 결과 처리
            if (ch != '\n' && ch != '\r') {
                System.out.print((char) ch);
                sb.append((char) ch);
            }
        }
        // 2. 프로세스 종료 대기
        int exitCode = process.waitFor();
        System.out.println("종료 코드: " + exitCode);

        // 3. 프로세스 강제 종료
        process.destroy();

        // 4. 종료 코드 확인
        int exitCode1 = process.exitValue();
        System.out.println("외부 프로그램 종료 코드: " + exitCode1);

        return sb.toString();
    }




}
